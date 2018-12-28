/**
 * 
 */
package com.everwing.coreservice.platform.core.serialization;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.everwing.coreservice.common.dto.LoginRslt;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SerializationOptimizerImpl implements SerializationOptimizer {

	@SuppressWarnings("rawtypes")
	@Override
	public Collection<Class> getSerializableClasses() {
		List<Class> classes = new LinkedList<Class>();
		classes.add(LoginRslt.class);
		
        return classes;
	}

}
