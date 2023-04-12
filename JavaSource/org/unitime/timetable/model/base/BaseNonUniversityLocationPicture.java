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

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.unitime.timetable.model.LocationPicture;
import org.unitime.timetable.model.NonUniversityLocation;
import org.unitime.timetable.model.NonUniversityLocationPicture;

/**
 * Do not change this class. It has been automatically generated using ant create-model.
 * @see org.unitime.commons.ant.CreateBaseModelFromXml
 */
@MappedSuperclass
public abstract class BaseNonUniversityLocationPicture extends LocationPicture implements Serializable {
	private static final long serialVersionUID = 1L;

	private NonUniversityLocation iLocation;

	public BaseNonUniversityLocationPicture() {
	}

	public BaseNonUniversityLocationPicture(Long uniqueId) {
		setUniqueId(uniqueId);
	}


	@ManyToOne(optional = false)
	@JoinColumn(name = "location_id", nullable = false)
	public NonUniversityLocation getLocation() { return iLocation; }
	public void setLocation(NonUniversityLocation location) { iLocation = location; }

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof NonUniversityLocationPicture)) return false;
		if (getUniqueId() == null || ((NonUniversityLocationPicture)o).getUniqueId() == null) return false;
		return getUniqueId().equals(((NonUniversityLocationPicture)o).getUniqueId());
	}

	@Override
	public int hashCode() {
		if (getUniqueId() == null) return super.hashCode();
		return getUniqueId().hashCode();
	}

	@Override
	public String toString() {
		return "NonUniversityLocationPicture["+getUniqueId()+"]";
	}

	public String toDebugString() {
		return "NonUniversityLocationPicture[" +
			"\n	ContentType: " + getContentType() +
			"\n	DataFile: " + getDataFile() +
			"\n	FileName: " + getFileName() +
			"\n	Location: " + getLocation() +
			"\n	TimeStamp: " + getTimeStamp() +
			"\n	Type: " + getType() +
			"\n	UniqueId: " + getUniqueId() +
			"]";
	}
}
