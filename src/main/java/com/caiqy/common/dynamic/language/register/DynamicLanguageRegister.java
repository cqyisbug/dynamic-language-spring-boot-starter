/*
 * Copyright [2021] [caiqy]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caiqy.common.dynamic.language.register;

import com.caiqy.common.dynamic.language.annotation.EnableDynamicLanguage;
import com.caiqy.common.dynamic.language.annotation.GroovyDynamicLanguageService;
import com.caiqy.common.dynamic.language.support.CqyScriptFactoryPostProcessor;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.Nullable;
import org.springframework.scripting.config.LangNamespaceUtils;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 动态语言注册器
 *
 * @author caiqy
 */
public class DynamicLanguageRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    /**
     * The unique name under which the internally managed {@link CqyScriptFactoryPostProcessor} is
     * registered in the {@link BeanDefinitionRegistry}.
     */
    private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME =
            "com.caiqy.common.dynamic.language.support.CqyScriptFactoryPostProcessor";

    private static final String GROOVY_SCRIPT_FACTORY_CLASS_NAME =
            "org.springframework.scripting.groovy.GroovyScriptFactory";

    private Environment environment;

    private ResourceLoader resourceLoader;

    //---------------------------------------------------------------------
    // Implementation of EnvironmentAware interfaces
    //---------------------------------------------------------------------

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    //---------------------------------------------------------------------
    // Implementation of ResourceLoaderAware interfaces
    //---------------------------------------------------------------------

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //---------------------------------------------------------------------
    // Implementation of ImportBeanDefinitionRegister interfaces
    //---------------------------------------------------------------------

    /**
     * 参考官方实现,添加相关bean定义
     *
     * @param importingClassMetadata import原数据
     * @param registry               bean注册中心
     * @see org.springframework.scripting.config.ScriptBeanDefinitionParser#parseInternal(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // Set up infrastructure.
        registerScriptFactoryPostProcessorIfNecessary(registry);

        // Create script factory bean definition.
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
//        Map<String, Object> attrs = importingClassMetadata
//                .getAnnotationAttributes(EnableDynamicLanguage.class.getName());

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(GroovyDynamicLanguageService.class));
        Set<String> basePackages = getBasePackages(importingClassMetadata);
        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {

                // verify annotated class is an interface
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(),
                        "@GroovyDynamicLangService can only be specified on an interface");

                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(GroovyDynamicLanguageService.class.getCanonicalName());

                //  don't worry, won't be null
                String id = (String) attributes.get("id");

                String value = resolveScriptSource(attributes);
                candidateComponent.setBeanClassName(GROOVY_SCRIPT_FACTORY_CLASS_NAME);
                candidateComponent.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, "groovy");

                long refreshCheckDelay = (long) attributes.get("refreshCheckDelay");
                candidateComponent.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, refreshCheckDelay);

                // Attach jdk proxy target class metadata.!!! 不使用cglib,cglib在刷新后强转会失败
                candidateComponent.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, false);

                // Add constructor arguments.
                ConstructorArgumentValues cav = candidateComponent.getConstructorArgumentValues();
                cav.addIndexedArgumentValue(0, value);
                // This is used for Groovy. It's a bean reference to a customizer bean.
                if (attributes.containsKey("customizerRef")) {
                    String customizerBeanName = (String) attributes.get("customizerRef");
                    if (!customizerBeanName.isEmpty()) {
                        cav.addIndexedArgumentValue(1, new RuntimeBeanReference(customizerBeanName));
                    }
                }

                String beanName = id;
                if (id.isEmpty()) {
                    beanName = BeanDefinitionReaderUtils.generateBeanName(
                            beanDefinition, registry, false);
                }

                registry.registerBeanDefinition(
                        beanName + "." + GroovyScriptFactory.class.getSimpleName(),
                        candidateComponent);

            }
        }
    }

    /**
     * Register a {@link CqyScriptFactoryPostProcessor} bean definition in the supplied
     * {@link BeanDefinitionRegistry} if the {@link CqyScriptFactoryPostProcessor} hasn't
     * already been registered.
     *
     * @param registry the {@link BeanDefinitionRegistry} to register the script processor with
     * @return the {@link CqyScriptFactoryPostProcessor} bean definition (new or already registered)
     * @see LangNamespaceUtils#registerScriptFactoryPostProcessorIfNecessary(org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    private BeanDefinition registerScriptFactoryPostProcessorIfNecessary(BeanDefinitionRegistry registry) {
        BeanDefinition beanDefinition;
        if (registry.containsBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME)) {
            beanDefinition = registry.getBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME);
        } else {
            beanDefinition = new RootBeanDefinition(CqyScriptFactoryPostProcessor.class);
            registry.registerBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME, beanDefinition);
        }
        return beanDefinition;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableDynamicLanguage.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();

        //  don't worry, won't be null
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    @Nullable
    private String resolveScriptSource(Map<String, Object> attributes) {
        String inlineScript = (String) attributes.get("inlineScript");
        String scriptSource = (String) attributes.get("scriptSource");

        if (inlineScript.isEmpty() && scriptSource.isEmpty()) {
            throw new IllegalStateException("Must specify either 'scriptSource' or 'inlineScript");
        } else if (!inlineScript.isEmpty() && !scriptSource.isEmpty()) {
            throw new IllegalStateException("Only one of 'script-source' and 'inline-script' should be specified.");
        } else if (!inlineScript.isEmpty()) {
            return "inline:" + inlineScript;
        } else {
            return scriptSource;
        }
    }
}
