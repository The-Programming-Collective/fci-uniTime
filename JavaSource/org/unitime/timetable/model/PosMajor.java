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
package org.unitime.timetable.model;



import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import java.util.List;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.unitime.timetable.model.base.BasePosMajor;
import org.unitime.timetable.model.dao.PosMajorDAO;




/**
 * @author Tomas Muller, Stephanie Schluttenhofer
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, include = "non-lazy")
@Table(name = "pos_major")
public class PosMajor extends BasePosMajor {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PosMajor () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PosMajor (java.lang.Long uniqueId) {
		super(uniqueId);
	}

/*[CONSTRUCTOR MARKER END]*/

    /** Request attribute name for available pos majors **/
    public static String POSMAJOR_ATTR_NAME = "posMajorList";  
    
	/**
	 * Retrieves all pos majors in the database for the academic session
	 * ordered by column name
	 * @param sessionId academic session
	 * @return Vector of PosMajor objects
	 */
    public static List getPosMajorList(Long sessionId) {
	    Session hibSession = new PosMajorDAO().getSession();
	    String query = "from PosMajor where academicArea.sessionId=:acadSessionId order by name";
	    Query q = hibSession.createQuery(query);
	    q.setParameter("acadSessionId", sessionId.longValue(), org.hibernate.type.LongType.INSTANCE);
		return q.list();
    }

    /**
     * Creates label of the format Name - Code
     * @return
     */

	@Transient
	public String getLabelNameCode() {
        return this.getName() + " - " + this.getCode();
    }

    /**
     * Creates label of the format Code - Name
     * @return
     */
	@Transient
    public String getLabelCodeName() {
        return this.getCode() + " - " + this.getName();
    }

    public static PosMajor findByCode(Long sessionId, String code) {
        return (PosMajor)new PosMajorDAO().
        getSession().
        createQuery(
                "select a from PosMajor a where "+
                "a.session.uniqueId=:sessionId and "+
                "a.code=:code").
         setParameter("sessionId", sessionId.longValue(), org.hibernate.type.LongType.INSTANCE).
         setParameter("code", code, org.hibernate.type.StringType.INSTANCE).
         setCacheable(true).
         uniqueResult(); 
    }

    public static PosMajor findByExternalIdAcadAreaExternalId(Long sessionId, String externalId, String academicArea) {
    	return(findByExternalIdAcadAreaExternalId(new PosMajorDAO().getSession(), sessionId, externalId, academicArea));
    }

    public static PosMajor findByExternalIdAcadAreaExternalId(Session hibSession, Long sessionId, String externalId, String academicArea) {
        return (PosMajor)hibSession.
        createQuery(
                "select a from PosMajor a inner join a.academicAreas as areas where "+
                "a.session.uniqueId=:sessionId and "+
                "a.externalUniqueId=:externalUniqueId and " +
                "areas.externalUniqueId = :academicArea").
         setParameter("sessionId", sessionId.longValue(), org.hibernate.type.LongType.INSTANCE).
         setParameter("externalUniqueId", externalId, org.hibernate.type.StringType.INSTANCE).
         setParameter("academicArea", academicArea, org.hibernate.type.StringType.INSTANCE).
         setCacheable(true).
         uniqueResult(); 
    }
    public static PosMajor findByCodeAcadAreaId(Long sessionId, String code, Long areaId) {
        if (areaId==null) return findByCode(sessionId, code);
        return (PosMajor)new PosMajorDAO().
        getSession().
        createQuery(
                "select p from PosMajor p inner join p.academicAreas a where "+
                "p.session.uniqueId=:sessionId and "+
                "a.uniqueId=:areaId and p.code=:code").
         setParameter("sessionId", sessionId.longValue(), org.hibernate.type.LongType.INSTANCE).
         setParameter("areaId", areaId.longValue(), org.hibernate.type.LongType.INSTANCE).
         setParameter("code", code, org.hibernate.type.StringType.INSTANCE).
         setCacheable(true).
         uniqueResult(); 
    }

    public static PosMajor findByCodeAcadAreaAbbv(Long sessionId, String code, String areaAbbv) {
    	return(findByCodeAcadAreaAbbv(new PosMajorDAO().getSession(), sessionId, code, areaAbbv));
    }
    
    public static PosMajor findByCodeAcadAreaAbbv(Session hibSession, Long sessionId, String code, String areaAbbv) {
        if (areaAbbv==null || areaAbbv.trim().length()==0) return findByCode(sessionId, code);
        return (PosMajor)hibSession.
        createQuery(
                "select p from PosMajor p inner join p.academicAreas a where "+
                "p.session.uniqueId=:sessionId and "+
                "a.academicAreaAbbreviation=:areaAbbv and p.code=:code").
         setParameter("sessionId", sessionId.longValue(), org.hibernate.type.LongType.INSTANCE).
         setParameter("areaAbbv", areaAbbv, org.hibernate.type.StringType.INSTANCE).
         setParameter("code", code, org.hibernate.type.StringType.INSTANCE).
         setCacheable(true).
         uniqueResult(); 
    }

    public Object clone() {
    	PosMajor m = new PosMajor();
    	m.setExternalUniqueId(getExternalUniqueId());
    	m.setCode(getCode());
    	m.setName(getName());
    	return m;
    }
    
    public boolean isUsed(org.hibernate.Session hibSession) {
    	return ((Number)(hibSession == null ? PosMajorDAO.getInstance().getSession() : hibSession).createQuery(
    			"select count(c) from Curriculum c inner join c.majors m where m.uniqueId = :majorId")
    			.setParameter("majorId", getUniqueId(), org.hibernate.type.LongType.INSTANCE).setCacheable(true).uniqueResult()).intValue() > 0;
    }
}
