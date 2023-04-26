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

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.unitime.timetable.model.CourseOffering;
import org.unitime.timetable.model.InstructorCoursePref;
import org.unitime.timetable.model.Preference;

/**
 * Do not change this class. It has been automatically generated using ant create-model.
 * @see org.unitime.commons.ant.CreateBaseModelFromXml
 */
@MappedSuperclass
public abstract class BaseInstructorCoursePref extends Preference implements Serializable {
	private static final long serialVersionUID = 1L;

	private CourseOffering iCourse;

	public BaseInstructorCoursePref() {
	}

	public BaseInstructorCoursePref(Long uniqueId) {
		setUniqueId(uniqueId);
	}


	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, includeLazy = false)
	public CourseOffering getCourse() { return iCourse; }
	public void setCourse(CourseOffering course) { iCourse = course; }

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof InstructorCoursePref)) return false;
		if (getUniqueId() == null || ((InstructorCoursePref)o).getUniqueId() == null) return false;
		return getUniqueId().equals(((InstructorCoursePref)o).getUniqueId());
	}

	@Override
	public int hashCode() {
		if (getUniqueId() == null) return super.hashCode();
		return getUniqueId().hashCode();
	}

	@Override
	public String toString() {
		return "InstructorCoursePref["+getUniqueId()+"]";
	}

	public String toDebugString() {
		return "InstructorCoursePref[" +
			"\n	Course: " + getCourse() +
			"\n	Note: " + getNote() +
			"\n	Owner: " + getOwner() +
			"\n	PrefLevel: " + getPrefLevel() +
			"\n	UniqueId: " + getUniqueId() +
			"]";
	}
}
