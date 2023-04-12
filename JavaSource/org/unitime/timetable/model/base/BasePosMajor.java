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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.unitime.timetable.model.AcademicArea;
import org.unitime.timetable.model.PosMajor;
import org.unitime.timetable.model.PosMajorConcentration;
import org.unitime.timetable.model.Session;

/**
 * Do not change this class. It has been automatically generated using ant create-model.
 * @see org.unitime.commons.ant.CreateBaseModelFromXml
 */
@MappedSuperclass
public abstract class BasePosMajor implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long iUniqueId;
	private String iExternalUniqueId;
	private String iCode;
	private String iName;

	private Session iSession;
	private Set<AcademicArea> iAcademicAreas;
	private Set<PosMajorConcentration> iConcentrations;

	public BasePosMajor() {
	}

	public BasePosMajor(Long uniqueId) {
		setUniqueId(uniqueId);
	}


	@Id
	@GenericGenerator(name = "pos_major_id", strategy = "org.unitime.commons.hibernate.id.UniqueIdGenerator", parameters = {
		@Parameter(name = "sequence", value = "pos_major_seq")
	})
	@GeneratedValue(generator = "pos_major_id")
	@Column(name="uniqueid")
	public Long getUniqueId() { return iUniqueId; }
	public void setUniqueId(Long uniqueId) { iUniqueId = uniqueId; }

	@Column(name = "external_uid", nullable = true, length = 40)
	public String getExternalUniqueId() { return iExternalUniqueId; }
	public void setExternalUniqueId(String externalUniqueId) { iExternalUniqueId = externalUniqueId; }

	@Column(name = "code", nullable = false, length = 40)
	public String getCode() { return iCode; }
	public void setCode(String code) { iCode = code; }

	@Column(name = "name", nullable = false, length = 100)
	public String getName() { return iName; }
	public void setName(String name) { iName = name; }

	@ManyToOne(optional = false)
	@JoinColumn(name = "session_id", nullable = false)
	public Session getSession() { return iSession; }
	public void setSession(Session session) { iSession = session; }

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "pos_acad_area_major",
		joinColumns = { @JoinColumn(name = "major_id") },
		inverseJoinColumns = { @JoinColumn(name = "academic_area_id") })
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, include = "non-lazy")
	public Set<AcademicArea> getAcademicAreas() { return iAcademicAreas; }
	public void setAcademicAreas(Set<AcademicArea> academicAreas) { iAcademicAreas = academicAreas; }
	public void addToacademicAreas(AcademicArea academicArea) {
		if (iAcademicAreas == null) iAcademicAreas = new HashSet<AcademicArea>();
		iAcademicAreas.add(academicArea);
	}

	@OneToMany(mappedBy = "major", cascade = {CascadeType.ALL}, orphanRemoval = true)
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, include = "non-lazy")
	public Set<PosMajorConcentration> getConcentrations() { return iConcentrations; }
	public void setConcentrations(Set<PosMajorConcentration> concentrations) { iConcentrations = concentrations; }
	public void addToconcentrations(PosMajorConcentration posMajorConcentration) {
		if (iConcentrations == null) iConcentrations = new HashSet<PosMajorConcentration>();
		iConcentrations.add(posMajorConcentration);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PosMajor)) return false;
		if (getUniqueId() == null || ((PosMajor)o).getUniqueId() == null) return false;
		return getUniqueId().equals(((PosMajor)o).getUniqueId());
	}

	@Override
	public int hashCode() {
		if (getUniqueId() == null) return super.hashCode();
		return getUniqueId().hashCode();
	}

	@Override
	public String toString() {
		return "PosMajor["+getUniqueId()+" "+getName()+"]";
	}

	public String toDebugString() {
		return "PosMajor[" +
			"\n	Code: " + getCode() +
			"\n	ExternalUniqueId: " + getExternalUniqueId() +
			"\n	Name: " + getName() +
			"\n	Session: " + getSession() +
			"\n	UniqueId: " + getUniqueId() +
			"]";
	}
}
