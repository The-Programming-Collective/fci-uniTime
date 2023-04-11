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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.unitime.timetable.model.base.BaseExternalBuilding;
import org.unitime.timetable.model.dao.ExternalBuildingDAO;




/**
 * @author Tomas Muller
 */
public class ExternalBuilding extends BaseExternalBuilding {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ExternalBuilding () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ExternalBuilding (java.lang.Long uniqueId) {
		super(uniqueId);
	}

/*[CONSTRUCTOR MARKER END]*/


	/**
	 * Get External Buildings for a session
	 * @param sessionId
	 * @return Hashtable
	 */
	public static Hashtable getBuildings(Long sessionId) {
		
		List bldgs = (new ExternalBuildingDAO()).getSession().createQuery(
				"from ExternalBuilding as b " + 
				"where b.session.uniqueId = " + sessionId.longValue()).
				list();
		
		Hashtable buildings = new Hashtable((int)(bldgs.size() * 1.25));
		Iterator l = bldgs.iterator();
		
		while(l.hasNext()) {
			ExternalBuilding bldg = (ExternalBuilding)l.next();
			buildings.put(bldg.getExternalUniqueId(), bldg);
		}
		
		return buildings;
	}

	public static ExternalBuilding findExternalBuildingForSession(String externalUniqueId, Session session){
		return ExternalBuildingDAO.getInstance().getSession()
				.createQuery("from ExternalBuilding where externalUniqueId = :externalId and session.uniqueId = :sessionId", ExternalBuilding.class)
				.setParameter("externalId", externalUniqueId)
				.setParameter("sessionId", session.getUniqueId())
				.setCacheable(true)
				.setMaxResults(1)
				.uniqueResult();
	}



	/**
	 * Retrieve an external building for a given abbreviation and academic session
	 * @param sessionId
	 * @param bldgAbbr
	 * @return null if no match found
	 */
	public static ExternalBuilding findByAbbv (Long sessionId, String bldgAbbr) {
		return ExternalBuildingDAO.getInstance().getSession()
				.createQuery("from ExternalBuilding where abbreviation = :bldgAbbr and session.uniqueId = :sessionId", ExternalBuilding.class)
				.setParameter("bldgAbbr", bldgAbbr)
				.setParameter("sessionId", sessionId)
				.setCacheable(true)
				.setMaxResults(1)
				.uniqueResult();
	}

	/**
	 * Retrieve an external room
	 * @param roomNbr
	 * @return null if no match found
	 */
	public ExternalRoom findRoom (String roomNbr) {

		ExternalRoom room = null;
		
		Iterator r = getRooms().iterator();
		while(r.hasNext()) {
			ExternalRoom rm = (ExternalRoom)r.next();
			if((roomNbr.trim()).equalsIgnoreCase(rm.getRoomNumber().trim())) {
				room = rm;
				break;
			}
		}
	    
		return room;
	}
}
