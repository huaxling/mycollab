/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.data.CustomViewScreenData;
import com.esofthead.mycollab.module.crm.data.NotificationSettingScreenData;
import com.esofthead.mycollab.module.crm.domain.CallWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.Task;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.AccountEvent.GotoRead;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.events.ActivityEvent.GotoCalendar;
import com.esofthead.mycollab.module.crm.events.ActivityEvent.GotoTodoList;
import com.esofthead.mycollab.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.events.CrmEvent;
import com.esofthead.mycollab.module.crm.events.CrmEvent.GotoHome;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent.GotoNotificationSetting;
import com.esofthead.mycollab.module.crm.events.DocumentEvent;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.module.crm.view.account.AccountAddPresenter;
import com.esofthead.mycollab.module.crm.view.account.AccountListPresenter;
import com.esofthead.mycollab.module.crm.view.account.AccountReadPresenter;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRootPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignAddPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignListPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignReadPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseAddPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseListPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseReadPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactAddPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactListPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactReadPresenter;
import com.esofthead.mycollab.module.crm.view.file.FileDashboardPresenter;
import com.esofthead.mycollab.module.crm.view.file.FileSearchResultPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadAddPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadConvertReadPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadListPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadReadPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.ContactRoleEditPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityAddPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityListPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityReadPresenter;
import com.esofthead.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.CallScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.esofthead.mycollab.module.crm.view.setting.CrmSettingPresenter;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmController implements IController {
	private static final long serialVersionUID = 1L;
	private CrmModule container;

	public CrmController(CrmModule container) {
		this.container = container;

		bindCrmEvents();
		bindAccountEvents();
		bindActivityEvents();
		bindCampaignEvents();
		bindContactEvents();
		bindLeadEvents();
		bindOpportunityEvents();
		bindCasesEvents();
		bindDocumentEvents();
		bindSettingEvents();
	}

	@SuppressWarnings("serial")
	private void bindCrmEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CrmEvent.GotoHome>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CrmEvent.GotoHome.class;
					}

					@Override
					public void handle(GotoHome event) {
						CrmHomePresenter presenter = PresenterResolver
								.getPresenter(CrmHomePresenter.class);
						presenter.go(container, null);
					}
				});
	}

	@SuppressWarnings("serial")
	private void bindAccountEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoList.class;
					}

					@Override
					public void handle(AccountEvent.GotoList event) {
						AccountListPresenter presenter = PresenterResolver
								.getPresenter(AccountListPresenter.class);

						AccountSearchCriteria criteria = new AccountSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(container,
								new ScreenData.Search<AccountSearchCriteria>(
										criteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoAdd.class;
					}

					@Override
					public void handle(AccountEvent.GotoAdd event) {
						AccountAddPresenter presenter = PresenterResolver
								.getPresenter(AccountAddPresenter.class);
						presenter.go(container,
								new ScreenData.Add<SimpleAccount>(
										new SimpleAccount()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoEdit.class;
					}

					@Override
					public void handle(AccountEvent.GotoEdit event) {
						AccountAddPresenter presenter = PresenterResolver
								.getPresenter(AccountAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoRead.class;
					}

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void handle(GotoRead event) {
						AccountReadPresenter presenter = PresenterResolver
								.getPresenter(AccountReadPresenter.class);
						presenter.go(container,
								new ScreenData.Preview(event.getData()));
					}
				});
	}

	private void bindActivityEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.GotoCalendar>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.GotoCalendar.class;
					}

					@Override
					public void handle(GotoCalendar event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container,
								new ActivityScreenData.GotoCalendar());
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.GotoTodoList>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.GotoTodoList.class;
					}

					@Override
					public void handle(GotoTodoList event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						ActivitySearchCriteria searchCriteria = new ActivitySearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(container,
								new ActivityScreenData.GotoActivityList(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskAdd>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskAdd.class;
					}

					@Override
					public void handle(ActivityEvent.TaskAdd event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new AssignmentScreenData.Add(
								new SimpleTask()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskEdit>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskEdit.class;
					}

					@Override
					public void handle(ActivityEvent.TaskEdit event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						Task task = null;
						if (event.getData() instanceof Integer) {
							TaskService taskService = ApplicationContextUtil
									.getSpringBean(TaskService.class);
							task = taskService.findById(
									(Integer) event.getData(),
									AppContext.getAccountId());
						} else if (event.getData() instanceof Task) {
							task = (Task) event.getData();
						} else {
							throw new MyCollabException(
									"Do not support event data "
											+ event.getData());
						}
						presenter.go(container, new AssignmentScreenData.Edit(
								task));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskRead>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskRead.class;
					}

					@Override
					public void handle(ActivityEvent.TaskRead event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new AssignmentScreenData.Read(
								(Integer) event.getData()));

					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingAdd>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingAdd.class;
					}

					@Override
					public void handle(ActivityEvent.MeetingAdd event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new MeetingScreenData.Add(
								new SimpleMeeting()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingEdit>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingEdit.class;
					}

					@Override
					public void handle(ActivityEvent.MeetingEdit event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);

						MeetingWithBLOBs meeting = null;
						if (event.getData() instanceof Integer) {
							MeetingService meetingService = ApplicationContextUtil
									.getSpringBean(MeetingService.class);
							meeting = meetingService.findById(
									(Integer) event.getData(),
									AppContext.getAccountId());
						} else if (event.getData() instanceof MeetingWithBLOBs) {
							meeting = (MeetingWithBLOBs) event.getData();
						} else {
							throw new MyCollabException(
									"Do not support event param: "
											+ event.getData());
						}

						presenter.go(container, new MeetingScreenData.Edit(
								meeting));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingRead>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingRead.class;
					}

					@Override
					public void handle(ActivityEvent.MeetingRead event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new MeetingScreenData.Read(
								(Integer) event.getData()));

					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallAdd>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallAdd.class;
					}

					@Override
					public void handle(ActivityEvent.CallAdd event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new CallScreenData.Add(
								new SimpleCall()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallEdit>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallEdit.class;
					}

					@Override
					public void handle(ActivityEvent.CallEdit event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);

						CallWithBLOBs call = null;
						if (event.getData() instanceof Integer) {
							CallService callService = ApplicationContextUtil
									.getSpringBean(CallService.class);
							call = callService.findById(
									(Integer) event.getData(),
									AppContext.getAccountId());
						} else if (event.getData() instanceof CallWithBLOBs) {
							call = (CallWithBLOBs) event.getData();
						} else {
							throw new MyCollabException(
									"Do not support event param: "
											+ event.getData());
						}
						presenter.go(container, new CallScreenData.Edit(call));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallRead>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallRead.class;
					}

					@Override
					public void handle(ActivityEvent.CallRead event) {
						ActivityRootPresenter presenter = PresenterResolver
								.getPresenter(ActivityRootPresenter.class);
						presenter.go(container, new CallScreenData.Read(
								(Integer) event.getData()));

					}
				});

	}

	@SuppressWarnings("serial")
	private void bindCampaignEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoList.class;
					}

					@Override
					public void handle(CampaignEvent.GotoList event) {
						CampaignListPresenter presenter = PresenterResolver
								.getPresenter(CampaignListPresenter.class);
						CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));

						presenter.go(container,
								new ScreenData.Search<CampaignSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoAdd.class;
					}

					@Override
					public void handle(CampaignEvent.GotoAdd event) {
						CampaignAddPresenter presenter = PresenterResolver
								.getPresenter(CampaignAddPresenter.class);
						presenter.go(container,
								new ScreenData.Add<SimpleCampaign>(
										new SimpleCampaign()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoEdit.class;
					}

					@Override
					public void handle(CampaignEvent.GotoEdit event) {
						CampaignAddPresenter presenter = PresenterResolver
								.getPresenter(CampaignAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(CampaignEvent.GotoRead event) {
						CampaignReadPresenter presenter = PresenterResolver
								.getPresenter(CampaignReadPresenter.class);
						presenter.go(container,
								new ScreenData.Preview(event.getData()));
					}
				});
	}

	@SuppressWarnings("serial")
	private void bindContactEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoList.class;
					}

					@Override
					public void handle(ContactEvent.GotoList event) {
						ContactListPresenter presenter = PresenterResolver
								.getPresenter(ContactListPresenter.class);

						ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(container,
								new ScreenData.Search<ContactSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoAdd.class;
					}

					@Override
					public void handle(ContactEvent.GotoAdd event) {
						ContactAddPresenter presenter = PresenterResolver
								.getPresenter(ContactAddPresenter.class);
						presenter.go(container,
								new ScreenData.Add<SimpleContact>(
										new SimpleContact()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoEdit.class;
					}

					@Override
					public void handle(ContactEvent.GotoEdit event) {
						ContactAddPresenter presenter = PresenterResolver
								.getPresenter(ContactAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(ContactEvent.GotoRead event) {
						ContactReadPresenter presenter = PresenterResolver
								.getPresenter(ContactReadPresenter.class);
						presenter.go(container,
								new ScreenData.Preview(event.getData()));
					}
				});
	}

	private void bindDocumentEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<DocumentEvent.GotoDashboard>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return DocumentEvent.GotoDashboard.class;
					}

					@Override
					public void handle(DocumentEvent.GotoDashboard event) {
						FileDashboardPresenter presenter = PresenterResolver
								.getPresenter(FileDashboardPresenter.class);
						presenter.go(container, null);
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<DocumentEvent.Search>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return DocumentEvent.Search.class;
					}

					@Override
					public void handle(DocumentEvent.Search event) {
						FileSearchResultPresenter presenter = PresenterResolver
								.getPresenter(FileSearchResultPresenter.class);
						presenter.go(container,
								new ScreenData<FileSearchCriteria>(
										(FileSearchCriteria) event.getData()));
					}
				});
	}

	private void bindSettingEvents() {
		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<CrmSettingEvent.GotoNotificationSetting>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return CrmSettingEvent.GotoNotificationSetting.class;
							}

							@Override
							public void handle(GotoNotificationSetting event) {
								CrmSettingPresenter presenter = PresenterResolver
										.getPresenter(CrmSettingPresenter.class);
								presenter
										.go(container,
												new NotificationSettingScreenData.Read());
							}
						});

		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<CrmSettingEvent.GotoCustomViewSetting>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return CrmSettingEvent.GotoCustomViewSetting.class;
							}

							@Override
							public void handle(
									CrmSettingEvent.GotoCustomViewSetting event) {
								CrmSettingPresenter presenter = PresenterResolver
										.getPresenter(CrmSettingPresenter.class);
								presenter.go(container,
										new CustomViewScreenData.Read());
							}
						});
	}

	@SuppressWarnings("serial")
	private void bindLeadEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoList.class;
					}

					@Override
					public void handle(LeadEvent.GotoList event) {
						LeadListPresenter presenter = PresenterResolver
								.getPresenter(LeadListPresenter.class);
						LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(container,
								new ScreenData.Search<LeadSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoAdd.class;
					}

					@Override
					public void handle(LeadEvent.GotoAdd event) {
						LeadAddPresenter presenter = PresenterResolver
								.getPresenter(LeadAddPresenter.class);
						presenter.go(container, new ScreenData.Add<SimpleLead>(
								new SimpleLead()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoEdit.class;
					}

					@Override
					public void handle(LeadEvent.GotoEdit event) {
						LeadAddPresenter presenter = PresenterResolver
								.getPresenter(LeadAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(LeadEvent.GotoRead event) {
						Object value = event.getData();
						SimpleLead lead;
						if (value instanceof Integer) {
							LeadService leadService = ApplicationContextUtil
									.getSpringBean(LeadService.class);
							lead = leadService.findById((Integer) value,
									AppContext.getAccountId());
						} else if (value instanceof SimpleLead) {
							lead = (SimpleLead) value;
						} else {
							throw new MyCollabException(
									"Do not support such param type");
						}

						if ("Converted".equals(lead.getStatus())) {
							LeadConvertReadPresenter presenter = PresenterResolver
									.getPresenter(LeadConvertReadPresenter.class);
							presenter.go(container,
									new ScreenData.Preview(lead));
						} else {
							LeadReadPresenter presenter = PresenterResolver
									.getPresenter(LeadReadPresenter.class);
							presenter.go(container,
									new ScreenData.Preview(lead));
						}

					}
				});
	}

	@SuppressWarnings("serial")
	private void bindOpportunityEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoList.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoList event) {
						OpportunityListPresenter presenter = PresenterResolver
								.getPresenter(OpportunityListPresenter.class);
						OpportunitySearchCriteria searchCriteria;
						if (event.getData() != null) {
							searchCriteria = (OpportunitySearchCriteria) event
									.getData();
						} else {
							searchCriteria = new OpportunitySearchCriteria();
							searchCriteria.setSaccountid(new NumberSearchField(
									SearchField.AND, AppContext.getAccountId()));
						}

						presenter
								.go(container,
										new ScreenData.Search<OpportunitySearchCriteria>(
												searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoAdd.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoAdd event) {
						OpportunityAddPresenter presenter = PresenterResolver
								.getPresenter(OpportunityAddPresenter.class);
						presenter.go(container,
								new ScreenData.Add<SimpleOpportunity>(
										new SimpleOpportunity()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoEdit.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoEdit event) {
						OpportunityAddPresenter presenter = PresenterResolver
								.getPresenter(OpportunityAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(OpportunityEvent.GotoRead event) {
						OpportunityReadPresenter presenter = PresenterResolver
								.getPresenter(OpportunityReadPresenter.class);
						presenter.go(container,
								new ScreenData.Preview(event.getData()));
					}
				});

		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<OpportunityEvent.GotoContactRoleEdit>() {
							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return OpportunityEvent.GotoContactRoleEdit.class;
							}

							@SuppressWarnings({ "unchecked", "rawtypes" })
							@Override
							public void handle(
									OpportunityEvent.GotoContactRoleEdit event) {
								ContactRoleEditPresenter presenter = PresenterResolver
										.getPresenter(ContactRoleEditPresenter.class);
								presenter.go(container,
										new ScreenData(event.getData()));
							}
						});
	}

	@SuppressWarnings("serial")
	private void bindCasesEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoList>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoList.class;
					}

					@Override
					public void handle(CaseEvent.GotoList event) {
						CaseListPresenter presenter = PresenterResolver
								.getPresenter(CaseListPresenter.class);

						CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(container,
								new ScreenData.Search<CaseSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoAdd>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoAdd.class;
					}

					@Override
					public void handle(CaseEvent.GotoAdd event) {
						CaseAddPresenter presenter = PresenterResolver
								.getPresenter(CaseAddPresenter.class);
						presenter.go(container, new ScreenData.Add<SimpleCase>(
								new SimpleCase()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoEdit>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoEdit.class;
					}

					@Override
					public void handle(CaseEvent.GotoEdit event) {
						CaseAddPresenter presenter = PresenterResolver
								.getPresenter(CaseAddPresenter.class);
						presenter.go(container, new ScreenData.Edit<Object>(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoRead>() {
					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(CaseEvent.GotoRead event) {
						CaseReadPresenter presenter = PresenterResolver
								.getPresenter(CaseReadPresenter.class);
						presenter.go(container,
								new ScreenData.Preview(event.getData()));
					}
				});
	}
}
