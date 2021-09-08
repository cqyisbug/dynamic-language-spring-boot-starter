package com.caiqy.common.dynamic.language.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;

/**
 * 扩展部分官方实现
 *
 * @author caiqy
 */
public class CqyScriptFactoryPostProcessor extends ScriptFactoryPostProcessor {

    Logger logger = LoggerFactory.getLogger(CqyScriptFactoryPostProcessor.class);

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        try {
            return super.postProcessBeforeInstantiation(beanClass, beanName);
        } catch (BeanCreationException e) {
            // just give a warning message before return null
            logger.warn(e.getMessage());
            return null;
        }
    }

    @Override
    protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator) {
        // add self protocol here
        return super.getScriptSource(beanName, scriptSourceLocator);
    }
}
