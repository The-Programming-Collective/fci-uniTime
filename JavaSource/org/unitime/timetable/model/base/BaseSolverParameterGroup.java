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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.unitime.timetable.model.SolverParameterDef;
import org.unitime.timetable.model.SolverParameterGroup;

/**
 * Do not change this class. It has been automatically generated using ant create-model.
 * @see org.unitime.commons.ant.CreateBaseModelFromXml
 */
@MappedSuperclass
public abstract class BaseSolverParameterGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long iUniqueId;
	private String iName;
	private String iDescription;
	private Integer iOrder;
	private Integer iType;

	private Set<SolverParameterDef> iParameters;

	public BaseSolverParameterGroup() {
	}

	public BaseSolverParameterGroup(Long uniqueId) {
		setUniqueId(uniqueId);
	}


	@Id
	@GenericGenerator(name = "solver_parameter_group_id", strategy = "org.unitime.commons.hibernate.id.UniqueIdGenerator", parameters = {
		@Parameter(name = "sequence", value = "solver_parameter_group_seq")
	})
	@GeneratedValue(generator = "solver_parameter_group_id")
	@Column(name="uniqueid")
	public Long getUniqueId() { return iUniqueId; }
	public void setUniqueId(Long uniqueId) { iUniqueId = uniqueId; }

	@Column(name = "name", nullable = true, length = 100)
	public String getName() { return iName; }
	public void setName(String name) { iName = name; }

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() { return iDescription; }
	public void setDescription(String description) { iDescription = description; }

	@Column(name = "ord", nullable = true, length = 4)
	public Integer getOrder() { return iOrder; }
	public void setOrder(Integer order) { iOrder = order; }

	@Column(name = "param_type", nullable = true, length = 10)
	public Integer getType() { return iType; }
	public void setType(Integer type) { iType = type; }

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = {CascadeType.ALL})
	@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, includeLazy = false)
	public Set<SolverParameterDef> getParameters() { return iParameters; }
	public void setParameters(Set<SolverParameterDef> parameters) { iParameters = parameters; }
	public void addToparameters(SolverParameterDef solverParameterDef) {
		if (iParameters == null) iParameters = new HashSet<SolverParameterDef>();
		iParameters.add(solverParameterDef);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof SolverParameterGroup)) return false;
		if (getUniqueId() == null || ((SolverParameterGroup)o).getUniqueId() == null) return false;
		return getUniqueId().equals(((SolverParameterGroup)o).getUniqueId());
	}

	@Override
	public int hashCode() {
		if (getUniqueId() == null) return super.hashCode();
		return getUniqueId().hashCode();
	}

	@Override
	public String toString() {
		return "SolverParameterGroup["+getUniqueId()+" "+getName()+"]";
	}

	public String toDebugString() {
		return "SolverParameterGroup[" +
			"\n	Description: " + getDescription() +
			"\n	Name: " + getName() +
			"\n	Order: " + getOrder() +
			"\n	Type: " + getType() +
			"\n	UniqueId: " + getUniqueId() +
			"]";
	}
}
