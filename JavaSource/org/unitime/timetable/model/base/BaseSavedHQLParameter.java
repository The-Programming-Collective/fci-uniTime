/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/
package org.unitime.timetable.model.base;

import jakarta.persistence.Column;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;

import org.unitime.timetable.model.SavedHQLParameter;

/**
 * Do not change this class. It has been automatically generated using ant create-model.
 * @see org.unitime.commons.ant.CreateBaseModelFromXml
 */
@MappedSuperclass
@IdClass(SavedHQLParameterId.class)
public abstract class BaseSavedHQLParameter extends SavedHQLParameterId {
	private static final long serialVersionUID = 1L;

	private String iLabel;
	private String iType;
	private String iDefaultValue;

	@Column(name = "label", nullable = true, length = 256)
	public String getLabel() { return iLabel; }
	public void setLabel(String label) { iLabel = label; }

	@Column(name = "type", nullable = false, length = 2048)
	public String getType() { return iType; }
	public void setType(String type) { iType = type; }

	@Column(name = "default_value", nullable = true, length = 2048)
	public String getDefaultValue() { return iDefaultValue; }
	public void setDefaultValue(String defaultValue) { iDefaultValue = defaultValue; }

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof SavedHQLParameter)) return false;
		SavedHQLParameter savedHQLParameter = (SavedHQLParameter)o;
		if (getSavedHQL() == null || savedHQLParameter.getSavedHQL() == null || !getSavedHQL().equals(savedHQLParameter.getSavedHQL())) return false;
		if (getName() == null || savedHQLParameter.getName() == null || !getName().equals(savedHQLParameter.getName())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (getSavedHQL() == null || getName() == null) return super.hashCode();
		return getSavedHQL().hashCode() ^ getName().hashCode();
	}

	public String toString() {
		return "SavedHQLParameter[" + getSavedHQL() + ", " + getName() + "]";
	}

	public String toDebugString() {
		return "SavedHQLParameter[" +
			"\n	DefaultValue: " + getDefaultValue() +
			"\n	Label: " + getLabel() +
			"\n	Name: " + getName() +
			"\n	SavedHQL: " + getSavedHQL() +
			"\n	Type: " + getType() +
			"]";
	}
}
