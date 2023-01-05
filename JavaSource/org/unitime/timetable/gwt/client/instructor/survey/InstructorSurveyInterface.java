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
package org.unitime.timetable.gwt.client.instructor.survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.unitime.timetable.gwt.client.instructor.InstructorAvailabilityWidget.InstructorAvailabilityModel;
import org.unitime.timetable.gwt.command.client.GwtRpcRequest;
import org.unitime.timetable.gwt.command.client.GwtRpcResponse;
import org.unitime.timetable.gwt.shared.ClassAssignmentInterface.IdValue;
import org.unitime.timetable.gwt.shared.CurriculumInterface.CourseInterface;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tomas Muller
 */
public class InstructorSurveyInterface implements IsSerializable {

	public static class InstructorSurvey implements GwtRpcResponse {
		private Long iId;
		private String iExternalId;
		private String iFormattedName;
		private String iEmail;
		private List<InstructorDepartment> iDepartments;
		private InstructorTimePreferencesModel iTimePrefs;
		private List<Preferences> iRoomPrefs;
		private Preferences iDistPrefs;
		private List<PrefLevel> iPrefLevels;
		private List<Course> iCourses;
		private List<CustomField> iCustomFields;
		
		public InstructorSurvey() {}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		public String getExternalId() { return iExternalId; }
		public void setExternalId(String externalId) { iExternalId = externalId; }
		public String getFormattedName() { return iFormattedName; }
		public void setFormattedName(String name) { iFormattedName = name; }
		
		public boolean hasEmail() { return iEmail != null && !iEmail.isEmpty(); }
		public String getEmail() { return iEmail; }
		public void setEmail(String email) { iEmail = email; }
		
		public boolean hasDepartments() { return iDepartments != null && !iDepartments.isEmpty(); }
		public boolean hasDepartment(Long id) {
			if (iDepartments == null || id == null) return false;
			for (InstructorDepartment d: iDepartments)
				if (d.getId().equals(id)) return true;
			return false;
		}
		public List<InstructorDepartment> getDepartments() { return iDepartments; }
		public void addDepartment(InstructorDepartment dept) {
			if (iDepartments == null) iDepartments = new ArrayList<InstructorDepartment>();
			iDepartments.add(dept);
		}
		
		public InstructorTimePreferencesModel getTimePrefs() { return iTimePrefs; }
		public void setTimePrefs(InstructorTimePreferencesModel timePrefs) { iTimePrefs = timePrefs; }
		
		public boolean hasRoomPreferences() { return iRoomPrefs != null && !iRoomPrefs.isEmpty(); }
		public List<Preferences> getRoomPreferences() { return iRoomPrefs; }
		public void addRoomPreference(Preferences pref) {
			if (iRoomPrefs == null) iRoomPrefs = new ArrayList<Preferences>();
			iRoomPrefs.add(pref);
		}
		
		public boolean hasDistributionPreferences() { return iDistPrefs != null && iDistPrefs.hasItems(); }
		public Preferences getDistributionPreferences() { return iDistPrefs; }
		public void setDistributionPreferences(Preferences distPrefs) { iDistPrefs = distPrefs; }
		
		public void addPrefLevel(PrefLevel prefLevel) {
			if (iPrefLevels == null) iPrefLevels = new ArrayList<PrefLevel>();
			iPrefLevels.add(prefLevel);
		}
		public List<PrefLevel> getPrefLevels() { return iPrefLevels; }
		
		public boolean hasCourses() { return iCourses != null && !iCourses.isEmpty(); }
		public List<Course> getCourses() { return iCourses; }
		public void addCourse(Course course) {
			if (iCourses == null) iCourses = new ArrayList<Course>();
			iCourses.add(course);
		}
		
		public List<CustomField> getCustomFields() { return iCustomFields; }
		public boolean hasCustomFields() { return iCustomFields != null && !iCustomFields.isEmpty(); }
		public void addCustomField(CustomField f) {
			if (iCustomFields == null) iCustomFields = new ArrayList<CustomField>();
			iCustomFields.add(f);
		}
	}
	
	public static class Preferences implements IsSerializable, Comparable<Preferences> {
		private Long iId;
		private String iType;
		private TreeSet<IdLabel> iItems;
		
		public Preferences() {}
		public Preferences(Long id, String type) {
			iId = id; iType = type;
		}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		public String getType() { return iType; }
		public void setType(String type) { iType = type; }
		
		public boolean hasItems() { return iItems != null && !iItems.isEmpty(); }
		public Set<IdLabel> getItems() { return iItems; }
		public IdLabel addItem(Long id, String label, String description) {
			if (iItems == null) iItems = new TreeSet<IdLabel>();
			IdLabel item = new IdLabel(id, label, description);
			if (!iItems.contains(item)) {
				iItems.add(item);
				return item;
			}
			return null;
		}
		
		@Override
		public int hashCode() { return getId().hashCode(); }
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof IdValue)) return false;
			return getId().equals(((IdValue)o).getId());
		}
		@Override
		public int compareTo(Preferences other) {
			return getType().compareTo(other.getType());
		}
	}
	
	public static class IdLabel implements IsSerializable, Comparable<IdLabel> {
		private Long iId;
		private String iLabel;
		private String iDescription;
		private Set<Long> iAllowedPrefs = null;

		public IdLabel() {}
		public IdLabel(Long id, String label, String description) {
			iId = id; iLabel = label; iDescription = description;
		}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		public String getLabel() { return iLabel; }
		public void setLabel(String label) { iLabel = label; }
		
		@Override
		public int hashCode() { return getId().hashCode(); }
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof IdValue)) return false;
			return getId().equals(((IdValue)o).getId());
		}
		@Override
		public int compareTo(IdLabel other) {
			return getLabel().compareTo(other.getLabel());
		}
		
		public boolean hasAllowedPrefs() { return iAllowedPrefs != null; }
		public void addAllowedPref(Long id) {
			if (iAllowedPrefs == null) iAllowedPrefs = new HashSet<Long>();
			iAllowedPrefs.add(id);
		}
		public boolean isAllowedPref(Long id) {
			if (id == null || iAllowedPrefs == null) return true;
			return iAllowedPrefs.contains(id);
		}
		
		public boolean hasDescription() { return iDescription != null && !iDescription.isEmpty(); }
		public String getDescription() { return iDescription; }
		public void setDescription(String description) { iDescription = description; }
	}
	
	public static class InstructorDepartment implements IsSerializable {
		private Long iId;
		private String iLabel;
		private IdLabel iPosition;
		
		public InstructorDepartment() {}
		public InstructorDepartment(Long id, String label, IdLabel position) {
			iId = id; iLabel = label; iPosition = position;
		}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		public String getLabel() { return iLabel; }
		public void setLabel(String label) { iLabel = label; }
		
		public boolean hasPosition() { return iPosition != null; }
		public IdLabel getPosition() { return iPosition; }
		public void setPosition(IdLabel position) { iPosition = position; }
	}
	
	public static class PrefLevel implements IsSerializable {
		private Long iId;
		private String iLabel;
		private String iTitle;
		private String iColor;
		private String iCode;
		
		public PrefLevel() {}
		public PrefLevel(Long id, String code, String label, String title, String color) {
			iId = id; iCode = code; iLabel = label; iTitle = title; iColor = color;
		}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		public String getCode() { return iCode; }
		public void setCode(String code) { iCode = code;} 
		public String getLabel() { return iLabel; }
		public void setLabel(String label) { iLabel = label; }
		public String getTitle() { return iTitle; }
		public void setTitle(String title) { iTitle = title; }
		public String getColor() { return iColor; }
		public void setColor(String color) { iColor = color; }
		public boolean isHard() { return "R".equals(iCode) || "P".equals(iCode); }
	}
	
	public static class InstructorSurveyRequest implements GwtRpcRequest<InstructorSurvey> {
		private String iExternalId;
		
		public InstructorSurveyRequest() {}
		public InstructorSurveyRequest(String externalId) { iExternalId = externalId; }
		
		public String getExternalId() { return iExternalId; }
		public void setExternalId(String externalId) { iExternalId = externalId; }
	}
	
	public static class InstructorTimePreferencesModel extends InstructorAvailabilityModel {
	}
	
	public static class CustomField implements IsSerializable {
		private Long iId;
		private String iName;
		private int iLength;
		
		public CustomField() {}
		public CustomField(Long id, String name, int length) {
			iId = id; iName = name; iLength = length;
		}
		
		public Long getId() { return iId; }
		public void setId(Long id) { iId = id; }
		
		public String getName() { return iName; }
		public void setName(String name) { iName = name; }
		
		public int getLength() { return iLength; }
		public void setLength(int length) { iLength = length; }
		
		@Override
		public int hashCode() { return getName().hashCode(); }
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof CustomField)) return false;
			return getId().equals(((CustomField)o).getId());
		}
	}
	
	public static class Course extends CourseInterface {
		private String iCourseTitle;
		private String iNote;
		private String iSection;
		private Map<Long, String> iCustoms;
		
		public Course() {
			super();
		}

		public boolean hasCourseTitle() { return iCourseTitle != null && !iCourseTitle.isEmpty(); }
		public String getCourseTitle() { return iCourseTitle; }
		public void setCoruseTitle(String courseTitle) { iCourseTitle = courseTitle; }
		
		public boolean hasSection() { return iSection != null && !iSection.isEmpty(); }
		public String getSection() { return iSection; }
		public void setSection(String section) { iSection = section; }
		
		public boolean hasNote() { return iNote != null && !iNote.isEmpty(); }
		public String getNote() { return iNote; }
		public void setNote(String note) { iNote = note; }
		
		public boolean hasCustomField(CustomField f) {
			String val = getCustomField(f);
			return val != null && !val.isEmpty();
		}
		public String getCustomField(CustomField f) {
			if (iCustoms == null) return null;
			return iCustoms.get(f.getId());
		}
		public void setCustomField(CustomField f, String value) {
			if (iCustoms == null) iCustoms = new HashMap<Long, String>();
			if (value != null)
				iCustoms.put(f.getId(), value);
			else
				iCustoms.remove(f.getId());
		}
	}
	
	public static enum CourseColumn {
		COURSE, SECTION, CUSTOM,
	}
}