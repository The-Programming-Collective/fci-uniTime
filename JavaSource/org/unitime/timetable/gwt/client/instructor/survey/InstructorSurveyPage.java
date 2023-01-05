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
import java.util.Collection;
import java.util.List;

import org.unitime.timetable.gwt.client.instructor.survey.InstructorSurveyInterface.Course;
import org.unitime.timetable.gwt.client.instructor.survey.InstructorSurveyInterface.IdLabel;
import org.unitime.timetable.gwt.client.instructor.survey.InstructorSurveyInterface.InstructorSurvey;
import org.unitime.timetable.gwt.client.instructor.survey.InstructorSurveyInterface.PrefLevel;
import org.unitime.timetable.gwt.client.instructor.survey.InstructorSurveyInterface.Preferences;
import org.unitime.timetable.gwt.client.page.UniTimeNotifications;
import org.unitime.timetable.gwt.client.rooms.RoomSharingWidget;
import org.unitime.timetable.gwt.client.widgets.LoadingWidget;
import org.unitime.timetable.gwt.client.widgets.P;
import org.unitime.timetable.gwt.client.widgets.SimpleForm;
import org.unitime.timetable.gwt.client.widgets.UniTimeHeaderPanel;
import org.unitime.timetable.gwt.client.widgets.UniTimeTextBox;
import org.unitime.timetable.gwt.command.client.GwtRpcService;
import org.unitime.timetable.gwt.command.client.GwtRpcServiceAsync;
import org.unitime.timetable.gwt.resources.GwtConstants;
import org.unitime.timetable.gwt.resources.GwtMessages;
import org.unitime.timetable.gwt.resources.GwtResources;
import org.unitime.timetable.gwt.shared.RoomInterface.RoomSharingModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author Tomas Muller
 */
public class InstructorSurveyPage extends Composite {
	protected static final GwtMessages MESSAGES = GWT.create(GwtMessages.class);
	protected static final GwtResources RESOURCES =  GWT.create(GwtResources.class);
	protected static final GwtConstants CONSTANTS = GWT.create(GwtConstants.class);
	protected static GwtRpcServiceAsync RPC = GWT.create(GwtRpcService.class);
	
	private SimpleForm iPanel;
	private UniTimeHeaderPanel iHeader, iFooter;
	
	private UniTimeTextBox iEmail;
	private InstructorTimePreferences iTimePrefs;
	private Note iPrefsNote;
	private InstructorSurveyCourseTable iCourses;
	
	private InstructorSurveyInterface.InstructorSurvey iSurvey;
	
	public InstructorSurveyPage() {
		iPanel = new SimpleForm(3);
		iPanel.addStyleName("unitime-InstructorSurveyPage");
		
		LoadingWidget.showLoading(MESSAGES.waitLoadingPage());
		RPC.execute(new InstructorSurveyInterface.InstructorSurveyRequest(Location.getParameter("id")), new AsyncCallback<InstructorSurveyInterface.InstructorSurvey>() {
			@Override
			public void onFailure(Throwable t) {
				LoadingWidget.hideLoading();
				UniTimeNotifications.error(MESSAGES.failedToLoadPage(t.getMessage()), t);
			}

			@Override
			public void onSuccess(InstructorSurvey survey) {
				setValue(survey);
				LoadingWidget.hideLoading();
			}
		});
		
		initWidget(iPanel);
	}
	
	public void setValue(InstructorSurveyInterface.InstructorSurvey survey) {
		iSurvey = survey;
		iPanel.clear();
		
		iHeader = new UniTimeHeaderPanel(survey.getFormattedName());
		iHeader.addButton("save", MESSAGES.buttonSaveInstructorSurvey(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				UniTimeNotifications.info(MESSAGES.fieldSavedSuccessfully());
			}
		});
		iHeader.addButton("submit", MESSAGES.buttonSubmitInstructorSurvey(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent e) {
				UniTimeNotifications.info(MESSAGES.fieldSavedSuccessfully());
			}
		});
		iPanel.addHeaderRow(iHeader);
		
		iPanel.addRow(MESSAGES.propExternalId(), new Label(survey.getExternalId()));
		
		iEmail = new UniTimeTextBox();
		iEmail.addStyleName("email");
		if (survey.hasEmail()) iEmail.setText(survey.getEmail());
		iPanel.addRow(MESSAGES.propEmail(), iEmail);
		
		if (survey.hasDepartments()) {
			P depts = new P("departments");
			for (InstructorSurveyInterface.InstructorDepartment dept: survey.getDepartments()) {
				String label = dept.getLabel() + (dept.hasPosition() ? " (" + dept.getPosition().getLabel() + ")" : "");
				depts.add(new Label(label, false));
			}
			iPanel.addRow(MESSAGES.propDepartment(), depts);
		}
		
		iPanel.addHeaderRow(new UniTimeHeaderPanel(MESSAGES.sectGeneralPreferences()));
		iTimePrefs = new InstructorTimePreferences();
		iTimePrefs.setModel(survey.getTimePrefs());
		iTimePrefs.setMode(survey.getTimePrefs().getModes().get(0), true);
		iPanel.addRow(iTimePrefs.getPanel());
		iPanel.addRow("", iTimePrefs.getReason());
		if (survey.hasRoomPreferences()) {
			for (Preferences p: survey.getRoomPreferences()) {
				iPanel.addRow(p.getType() + ":", new PreferencesTable(p.getItems(), survey.getPrefLevels()));
			}
		}
		if (survey.hasDistributionPreferences()) {
			iPanel.addRow(MESSAGES.propDistributionPreferences(), new PreferencesTable(survey.getDistributionPreferences().getItems(), survey.getPrefLevels()));
		}
		iPrefsNote = new Note();
		iPanel.addRow(MESSAGES.propOtherPreferences(), iPrefsNote);
		
		iPanel.addHeaderRow(new UniTimeHeaderPanel(MESSAGES.sectCoursePreferences()));
		iCourses = new InstructorSurveyCourseTable(survey.getCustomFields());
		if (survey.hasCourses()) {
			for (Course ci: survey.getCourses()) {
				iCourses.addRow(ci);
			}
		}
		for (int i = 0; i < 5; i++) {
			iCourses.addRow(new Course());
		}
		int row = iPanel.addRow(iCourses);
		iPanel.getCellFormatter().getElement(row, 0).getStyle().setPadding(0, Unit.PX);
		
		iFooter = iHeader.clonePanel("");
		iPanel.addBottomRow(iFooter);
	}
	
	static class PreferencesTable extends P {
		static int lastId = 0;
		ChangeHandler iChangeHandler;
		HandlerRegistration iHandlerRegistration;
		
		PreferencesTable(final Collection<IdLabel> items, List<PrefLevel> options) {
			super("preference-table");
			add(new PreferenceLine(items, options));
			
			iChangeHandler = new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent e) {
					if (((PreferenceLine)getWidget(getWidgetCount() - 1)).getId() != null) {
						PreferenceLine p = new PreferenceLine(items, options);
						add(p);
						iHandlerRegistration.removeHandler();
						iHandlerRegistration = p.addChangeHandler(iChangeHandler);
						fixButtons();
					}
				}
			};
			fixButtons();
		}
		
		protected void fixButtons() {
			if (iHandlerRegistration != null) 
				iHandlerRegistration.removeHandler();
			for (int i = 0; i < getWidgetCount(); i++) {
				PreferenceLine p = ((PreferenceLine)getWidget(i)); 
				if (i < getWidgetCount() - 1) {
					p.setButtonAdd(false);
				} else {
					p.setButtonAdd(true);
					iHandlerRegistration = p.addChangeHandler(iChangeHandler);		
				}
			}
		}
		
		class PreferenceLine extends P implements HasChangeHandlers {
			ListBox iList;
			List<RadioButton> iRadios;
			Image iButton;
			boolean iButtonAdd = false;
			List<PrefLevel> iOptions;
			Collection<IdLabel> iItems;
			P iDescription;
			Note iReason;
			
			PreferenceLine(Collection<IdLabel> items, List<PrefLevel> options) {
				super("preference-line");
				P line1 = new P("first-line");
				P line2 = new P("second-line");
				add(line1); add(line2);
				
				iOptions = options;
				iItems = items;

				iList = new ListBox();
				iList.addItem("-", "");
				for (IdLabel item: items)
					iList.addItem(item.getLabel(), item.getId().toString());
				iList.addStyleName("preference-cell");
				iList.addChangeHandler(new ChangeHandler() {
					@Override
					public void onChange(ChangeEvent event) {
						fixOptions();
						PreferenceLine.this.fireEvent(event);
					}
				});
				line1.add(iList);

				iRadios = new ArrayList<RadioButton>();
				for (final PrefLevel option: options) {
					RadioButton opt = new RadioButton("pref" + lastId, option.getLabel());
					opt.setTitle(option.getTitle());
					opt.getElement().getStyle().setColor(option.getColor());
					opt.addStyleName("preference-cell");
					iRadios.add(opt);
					line1.add(opt);
					opt.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
						@Override
						public void onValueChange(ValueChangeEvent<Boolean> event) {
							ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), PreferenceLine.this);
							PrefLevel level = PreferenceLine.this.getSelection();
							iReason.setVisible(level != null && level.isHard());
							if (iReason.isVisible()) {
								iDescription.removeStyleName("wide-description");
								iReason.setHint(MESSAGES.hintProvideReasonFor(level.getTitle(), iList.getSelectedItemText()));
							} else
								iDescription.addStyleName("wide-description");
						}
					});
				}
				
				iButton = new Image(RESOURCES.delete());
				iButton.setTitle(MESSAGES.titleDeleteRow());
				iButton.addStyleName("preference-cell");
				iButton.getElement().getStyle().setCursor(Cursor.POINTER);
				iButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (iButtonAdd) {
							PreferencesTable.this.add(new PreferenceLine(iItems, options));
						} else {
							PreferencesTable.this.remove(PreferenceLine.this);
						}
						PreferencesTable.this.fixButtons();
					}
				});
				line1.add(iButton);
				
				iDescription = new P("description", "wide-description");
				iDescription.setVisible(false);
				line2.add(iDescription);
				
				iReason = new Note(); iReason.addStyleName("reason");
				iReason.setCharacterWidth(49);
				iReason.setVisible(false);
				line2.add(iReason);

				fixOptions();
				lastId ++;
			}
			
			protected IdLabel getItem() {
				String id = iList.getSelectedValue();
				if (id == null || id.isEmpty()) return null;
				for (IdLabel item: iItems)
					if (item.getId().toString().equals(id)) return item;
				return null;
			}
			
			protected void fixOptions() {
				IdLabel item = getItem();
				if (item != null && item.hasDescription()) {
					iDescription.setVisible(true);
					iDescription.setHTML(item.getDescription());
					iReason.setCharacterWidth(49);
				} else {
					iDescription.setVisible(false);
					iReason.setCharacterWidth(95);
				}
				int nbrVisible = 0;
				RadioButton lastVisible = null;
				PrefLevel level = null;
				for (int i = 0; i < iOptions.size(); i++) {
					RadioButton opt = iRadios.get(i);
					PrefLevel option = iOptions.get(i);
					if (item == null || !item.isAllowedPref(option.getId())) {
						opt.setEnabled(false);
						opt.setVisible(false);
						opt.setValue(null);
					} else {
						opt.setEnabled(true);
						opt.setVisible(true);
						lastVisible = opt;
						nbrVisible ++;
						if (Boolean.TRUE.equals(opt.getValue()))
							level = option;
					}
				}
				if (nbrVisible == 1) lastVisible.setValue(true);
				iReason.setVisible(level != null && level.isHard());
				if (iReason.isVisible()) {
					iDescription.removeStyleName("wide-description");
					iReason.setHint(MESSAGES.hintProvideReasonFor(level.getTitle(), iList.getSelectedItemText()));
				} else
					iDescription.addStyleName("wide-description");
			}
			
			public PrefLevel getSelection() {
				for (int i = 0; i < iOptions.size(); i++) {
					RadioButton opt = iRadios.get(i);
					if (Boolean.TRUE.equals(opt.getValue()))
						return iOptions.get(i);
				}
				return null;
			}
			
			public Long getId() {
				String id = iList.getSelectedValue();
				if (id == null || id.isEmpty()) return null;
				if (getSelection() == null) return null;
				return Long.valueOf(id);
			}

			public void setId(Long id) {
				if (id == null) {
					iList.setSelectedIndex(0);
				} else {
					for (int i = 1; i < iList.getItemCount(); i++) {
						if (iList.getValue(i).equals(id.toString())) {
							iList.setSelectedIndex(i);
							break;
						}
					}
				}
			}

			@Override
			public HandlerRegistration addChangeHandler(ChangeHandler handler) {
				return addDomHandler(handler, ChangeEvent.getType());
			}
			
			public void setButtonAdd(boolean add) {
				iButtonAdd = add;
				if (add) {
					iButton.setResource(RESOURCES.add());
					iButton.setTitle(MESSAGES.titleAddRow());
				} else {
					iButton.setResource(RESOURCES.delete());
					iButton.setTitle(MESSAGES.titleDeleteRow());
				}
			}
		}
		
	}
	
	public InstructorSurveyInterface.InstructorSurvey getValue() {
		return iSurvey;
	}
	
	public static class Note extends TextArea {
		Timer iTimer;
		private String iHint = "";
		
		public Note() {
			setStyleName("unitime-TextArea");
			setHeight("45px");
			setCharacterWidth(95);
			getElement().setAttribute("maxlength", "2048");
			
			iTimer = new Timer() {
				@Override
				public void run() {
					resizeNotes();
				}
			};
			addKeyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent event) {
					iTimer.schedule(10);
				}
			});
			addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					iTimer.schedule(10);
				}
			});
			addBlurHandler(new BlurHandler() {
				public void onBlur(BlurEvent event) {
					if (Note.super.getText().isEmpty()) {
						if (!iHint.isEmpty()) {
							Note.super.setText(iHint);
							addStyleName("notes-hint");
						}
					}
				}
			});
			addFocusHandler(new FocusHandler() {
				public void onFocus(FocusEvent event) {
					if (!iHint.isEmpty() && Note.super.getText().equals(iHint)) {
						Note.super.setText("");
						removeStyleName("notes-hint");
					}
				}
			});
		}
		
		@Override
		public String getText() {
			if (super.getText().equals(iHint)) return "";
			return super.getText();
		}
		
		@Override
		public void setText(String text) { 
			if (text == null || text.isEmpty()) {
				super.setText(iHint);
				if (!iHint.isEmpty())
					addStyleName("notes-hint");
				else
					removeStyleName("notes-hint");
			} else {
				super.setText(text);
				removeStyleName("notes-hint");
			}
		}
		
		public void resizeNotes() {
			if (!getText().isEmpty()) {
				setHeight(Math.max(45, getElement().getScrollHeight()) + "px");
			} else {
				setHeight("45px");
			}
		}
		
		public void setHint(String hint) {
			if (super.getText().equals(iHint)) {
				super.setText(hint);
				if (!hint.isEmpty())
					addStyleName("notes-hint");
				else
					removeStyleName("notes-hint");
			}
			iHint = hint;
		}
		
		public String getHint() { return iHint; }
		
		protected void checkHint() {
			if (getValue().isEmpty()) {
				
			}
		}
	}
	
	public static class InstructorTimePreferences extends RoomSharingWidget {
		Note iReason;
		
		public InstructorTimePreferences() {
			super(true, false);
			
			iReason = new Note(); iReason.addStyleName("prohibited-times-reason");
			iReason.setVisible(false);
			iReason.setHint(MESSAGES.hintProvideReasonForProhibitedTimes());
			
			addValueChangeHandler(new ValueChangeHandler<RoomSharingModel>() {
				@Override
				public void onValueChange(ValueChangeEvent<RoomSharingModel> event) {
					iReason.setVisible(event.getValue() != null && event.getValue().countOptions(-7l)>0);
				}
			});
		}
		
		public Note getReason() {
			return iReason;
		}
	}
}