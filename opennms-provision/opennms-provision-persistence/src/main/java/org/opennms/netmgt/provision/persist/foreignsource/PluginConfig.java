/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 *
 * Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 *
 */

package org.opennms.netmgt.provision.persist.foreignsource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * A PluginConfig represents a portion of a configuration that defines a reference
 * to a Java class "plugin" along with a set of parameters used to configure the
 * behavior of that plugin.
 * 
 * @author <a href="mailto:ranger@opennms.org">Benjamin Reed</a>
 * @author <a href="mailto:brozow@opennms.org">Matt Brozowski</a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="plugin")
public class PluginConfig implements Serializable, Comparable<PluginConfig> {
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name="name")
    private String m_name;

    @XmlAttribute(name="class")
    private String m_pluginClass;

    @XmlElement(name="parameter")
    private List<PluginParameter> m_parameters = new ArrayList<PluginParameter>();

    /**
     * Creates an empty plugin configuration.
     */
    public PluginConfig() {
    }
    
    /**
     * Creates a plugin configuration with the given name and class.
     * 
     * @param name the human-readable name of the plugin
     * @param clazz the name of the plugin's java class
     */
    public PluginConfig(String name, String clazz) {
        setName(name);
        setPluginClass(clazz);
    }
    
    public PluginConfig(PluginConfig pluginConfig) {
        setName(pluginConfig.getName());
        setPluginClass(pluginConfig.getPluginClass());
        setParameters(pluginConfig.getParameters());
    }

    /**
     * Get the name of the plugin.
     * 
     * @return the human-readable name of the plugin
     */
    public String getName() {
        return m_name;
    }
    /**
     * Sets the name of the plugin.
     * 
     * @param name the human-readable name to set
     */
    public void setName(String name) {
        m_name = name;
    }
    /**
     * Get the name of the plugin's java class.
     * 
     * @return the plugin's class name
     */
    public String getPluginClass() {
        return m_pluginClass;
    }
    /**
     * Set the name of the plugin's java class.
     * 
     * @param pluginClass the plugin class name to set
     */
    public void setPluginClass(String clazz) {
        m_pluginClass = clazz;
    }
    /**
     * Get a {@link List} of the plugin parameters.
     * @return the parameters
     */
    public List<PluginParameter> getParameterList() {
        return m_parameters;
    }
    
    /**
     * @param parameters the parameters to set
     */
    public void setParameterList(List<PluginParameter> list) {
        m_parameters = list;
    }
    
    /**
     * @return the parameters
     */
    public Map<String,String> getParameters() {
        Map<String,String> parms = new LinkedHashMap<String,String>();
        for (PluginParameter p : m_parameters) {
            parms.put(p.getKey(), p.getValue());
        }
        return Collections.unmodifiableMap(parms);
    }
    
    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, String> parameters) {
        m_parameters.clear();
        for (Entry<String,String> set : parameters.entrySet()) {
            m_parameters.add(new PluginParameter(set));
        }
    }

    /**
     * @param key the parameter name
     * @return the parameter value
     */
    public String getParameter(String key) {
        for (PluginParameter p : m_parameters) {
            if (p.getKey().equals(key)) {
                return p.getValue();
            }
        }
        return null;
    }

    /**
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addParameter(String key, String value) {
        for (int i = 0; i < m_parameters.size(); i++) {
            if (m_parameters.get(i).getKey().equals(key)) {
                m_parameters.set(i, new PluginParameter(key, value));
                return;
            }
        }
        m_parameters.add(new PluginParameter(key, value));
    }

    private String getParametersAsString() {
        StringBuilder sb = new StringBuilder();
        for (PluginParameter p : m_parameters) {
            sb.append(p.getKey()).append('=').append(p.getValue()).append('/');
        }
        sb.deleteCharAt(sb.length());
        return sb.toString();
    }

    public int compareTo(PluginConfig obj) {
        return new CompareToBuilder()
            .append(getName(), obj.getName())
            .append(getPluginClass(), obj.getPluginClass())
            .append(getParametersAsString(), obj.getParametersAsString())
            .toComparison();
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("name", getName())
            .append("plugin-class", getPluginClass())
            .append("parameters", getParametersAsString())
            .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PluginConfig) {
            PluginConfig other = (PluginConfig) obj;
            return new EqualsBuilder()
                .append(getName(), other.getName())
                .append(getPluginClass(), other.getPluginClass())
                .append(getParametersAsString(), other.getParametersAsString())
                .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(getName())
            .append(getPluginClass())
            .append(getParametersAsString())
            .toHashCode();
      }

}
