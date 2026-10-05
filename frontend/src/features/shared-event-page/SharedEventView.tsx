// Pathing
// _______
// src/features/shared-event-page/SharedEventView.tsx

import { useLayoutEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import InputText from '../../shared/components/input-text/InputText'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useSharedEventView } from './SharedEventView.hooks'
import styles from './SharedEventView.module.css'

type EventTab = 'details' | 'conversation' | 'important' | 'settings' | 'resources' | 'checklist'

export default function SharedEventView() {
	const { content, language } = useTranslate()
	const copy = content.ownerEvent
	const { addContact, closeCurrentEvent, closeState, contactEmail, contactState, decideProposal, decisionState, event, hasProposalChanges, isPrimaryContact, loadEvent, message, messageState, messages, messagesContainerRef, proposalState, proposalValues, proposals, rejectionExplanation, rejectingProposalId, role, sendMessage, setContactEmail, setMessage, setProposalValue, setRejectionExplanation, setRejectingProposalId, state, submitEventProposals } = useSharedEventView()
	const [activeTab, setActiveTab] = useState<EventTab>('details')
	useLayoutEffect(() => {
		if (activeTab !== 'conversation' || !messagesContainerRef.current) return
		const container = messagesContainerRef.current
		const scrollToLatestMessage = () => { container.scrollTop = container.scrollHeight - container.clientHeight }
		scrollToLatestMessage()
		const animationFrame = window.requestAnimationFrame(scrollToLatestMessage)
		return () => window.cancelAnimationFrame(animationFrame)
	}, [activeTab, messages, messagesContainerRef])

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.loading}</p></PageContainer></main>
	if (state !== 'loaded' || !event) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{copy.error}</p><button type="button" onClick={() => void loadEvent()}>{copy.retry}</button></section></PageContainer></main>
	}

	const backPath = role === 'OWNER' ? '/owner/requests' : role === 'STAFF' ? '/staff/events' : '/dashboard/events/'
	const isClosed = event.status === 'CLOSED_BY_CUSTOMER' || event.status === 'CLOSED_BY_OWNER' || event.status === 'CANCELLED_BY_CUSTOMER'
	const mayMessage = !isClosed && (role === 'OWNER' || role === 'CUSTOMER')
	const mayProposeChanges = !isClosed && (role === 'OWNER' || (role === 'CUSTOMER' && isPrimaryContact))

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to={backPath}>← {copy.back}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{copy.eyebrow} · {event.status === 'AWAITING_APPROVAL' ? copy.statusAwaitingApproval
						: isClosed ? copy.statusClosed
							: event.status === 'BOOKED' ? copy.statusBooked
								: event.status === 'AWAITING_DEPOSIT' ? copy.statusAwaitingDeposit : copy.statusApproved}</p>
					<h1>{event.eventName || copy.defaultEventName}</h1>
					<p>{copy.description}</p>
				</header>

				<section className={styles.panel}>
					<nav className={styles.tabs} aria-label={copy.detailsTitle}>
						<TabButton activeTab={activeTab} tab="details" onSelect={setActiveTab}>{copy.detailsTab}</TabButton>
						{role !== 'STAFF' && <>
							<TabButton activeTab={activeTab} tab="conversation" onSelect={setActiveTab}>{copy.conversationTab}</TabButton>
							<TabButton activeTab={activeTab} tab="important" onSelect={setActiveTab}>{copy.importantTab}</TabButton>
						</>}
						{mayProposeChanges && <TabButton activeTab={activeTab} tab="settings" onSelect={setActiveTab}>{copy.settingsTab}</TabButton>}
						{role === 'OWNER' && <>
							<button className={styles.lockedTab} type="button" disabled>{copy.trelloTab}</button>
							<button className={styles.lockedTab} type="button" disabled>{copy.resourcesTab}</button>
							<button className={styles.lockedTab} type="button" disabled>{copy.checklistTab}</button>
						</>}
						{role === 'STAFF' && <>
							<TabButton activeTab={activeTab} tab="checklist" onSelect={setActiveTab}>{copy.checklistTab}</TabButton>
							<TabButton activeTab={activeTab} tab="resources" onSelect={setActiveTab}>{copy.resourcesTab}</TabButton>
						</>}
					</nav>

					{activeTab === 'details' && <section className={styles.tabContent}>
						<h2>{copy.detailsTitle}</h2>
						<section className={styles.generalDetails}>
							<h3>{copy.generalTitle}</h3>
							<dl className={styles.facts}>
								{event.customerEmail && <Fact label={copy.customerEmail} value={event.customerEmail} />}
								{event.primaryContactName && <Fact label={copy.createdBy} value={event.primaryContactName} />}
								<Fact label={copy.approvedAt} value={new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(event.approvedAt))} />
							</dl>
						</section>
						<hr className={styles.divider} />
						{role === 'STAFF' && <section className={styles.generalDetails}>
							<h3>{copy.operationalDetailsTitle}</h3>
							{event.operationalDetails.length === 0 && <p className={styles.readOnly}>{copy.noOperationalDetails}</p>}
							{event.operationalDetails.map((detail, index) => <article className={styles.proposal} key={`${index}-${detail.question}`}>
								<p><strong>{detail.question}</strong></p>
								<p>{detail.answer}</p>
							</article>)}
						</section>}
						<form className={styles.specificDetails} onSubmit={(formEvent) => { formEvent.preventDefault(); void submitEventProposals() }}>
							<h3>{copy.specificDetailsTitle}</h3>
							<InputText label={copy.eventName} name="event-name" placeholder={event.eventName || copy.defaultEventName} value={proposalValues.event_name} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('event_name', value)} />
							<InputText label={copy.guestCount} name="event-guest-count" type="number" min={1} max={5000} step={1} placeholder={event.expectedGuestCount == null ? '' : String(event.expectedGuestCount)} value={proposalValues.expected_guest_count} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('expected_guest_count', value)} />
							<InputText label={copy.requestedDate} name="event-requested-date" placeholder={event.requestedDate ?? ''} value={proposalValues.requested_date} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('requested_date', value)} />
							{proposalState === 'error' && <p className={styles.error} role="alert">{copy.proposalError}</p>}
							{mayProposeChanges && <button className={styles.primaryAction} type="submit" disabled={!hasProposalChanges || proposalState === 'submitting'}>{proposalState === 'submitting' ? copy.proposalSending : copy.proposeChange}</button>}
						</form>
						{mayProposeChanges && <section className={styles.contacts}>
							<h3>{copy.contactsTitle}</h3>
							<form className={styles.contactForm} onSubmit={addContact}>
								<InputText label={copy.contactEmailLabel} name="event-contact-email" type="email" required value={contactEmail} onChange={setContactEmail} />
								<button className={styles.secondaryAction} type="submit" disabled={contactState === 'adding' || !contactEmail.trim()}>{contactState === 'adding' ? copy.contactAdding : copy.addContact}</button>
								{contactState === 'added' && <p role="status">{copy.contactAdded}</p>}
								{contactState === 'error' && <p className={styles.error} role="alert">{copy.contactError}</p>}
							</form>
						</section>}
					</section>}

					{activeTab === 'conversation' && <section className={styles.conversationPanel}>
						<h2>{copy.conversationTitle}</h2>
						{mayMessage ? <>
							<div className={styles.messages} aria-live="polite" ref={messagesContainerRef}>
								{messages.map((item) => (
									<article className={`${styles.message} ${item.is_mine ? styles.ownMessage : styles.otherMessage} ${item.sender_type === 'OWNER' ? styles.ownerMessage : styles.customerMessage}`} key={item.id}>
										<p className={styles.messageMeta}><strong>{item.sender_name}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p>
										<p>{item.content}</p>
									</article>
								))}
								{messages.length === 0 && <p>{copy.noMessages}</p>}
							</div>
							<form className={styles.composer} onSubmit={sendMessage}>
								<label htmlFor="shared-event-message">{copy.messageLabel}</label>
								<textarea id="shared-event-message" value={message} maxLength={5000} onChange={(formEvent) => setMessage(formEvent.currentTarget.value)} />
								{messageState === 'error' && <p className={styles.error} role="alert">{copy.messageError}</p>}
								<button type="submit" disabled={messageState === 'sending' || !message.trim()}>{messageState === 'sending' ? copy.sending : copy.sendMessage}</button>
							</form>
						</> : <div className={styles.readOnly}><p>{copy.staffReadOnly}</p></div>}
					</section>}

					{activeTab === 'important' && <section className={styles.changeHistory}>
						<h2>{copy.changeHistoryTitle}</h2>
						{proposals.length === 0 && <p>{copy.noChanges}</p>}
						{proposals.map((proposal) => {
							const mayDecide = !isClosed && proposal.status === 'PENDING' && (role === 'OWNER'
								? proposal.proposer_party === 'CUSTOMER'
								: role === 'CUSTOMER' && isPrimaryContact && proposal.proposer_party === 'OWNER')
							const statusLabel = proposal.status === 'PENDING' ? copy.proposalPending
								: proposal.status === 'APPROVED' ? copy.proposalApproved
									: proposal.status === 'REJECTED' ? copy.proposalRejected : copy.proposalSuperseded
							return <article className={styles.proposal} key={proposal.id}>
								<p className={styles.proposalMeta}><strong>{proposal.proposer_name}</strong><time dateTime={proposal.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(proposal.created_at))}</time></p>
								<p>{proposal.old_value} → {proposal.new_value}</p>
								<p className={styles.proposalStatus}>{statusLabel} · {copy.customerApproval}: {proposal.customer_approved ? copy.approvalRecorded : copy.approvalWaiting} · {copy.ownerApproval}: {proposal.owner_approved ? copy.approvalRecorded : copy.approvalWaiting}</p>
								{proposal.rejection_explanation && <p>{copy.rejectionReason}: {proposal.rejection_explanation}</p>}
								{mayDecide && rejectingProposalId !== proposal.id && <div className={styles.proposalActions}>
									<button className={styles.primaryAction} type="button" disabled={decisionState === 'submitting'} onClick={() => void decideProposal(proposal.id, 'APPROVED')}>{copy.approveChange}</button>
									<button className={styles.secondaryAction} type="button" disabled={decisionState === 'submitting'} onClick={() => { setRejectingProposalId(proposal.id); setRejectionExplanation('') }}>{copy.rejectChange}</button>
								</div>}
								{mayDecide && rejectingProposalId === proposal.id && <form className={styles.rejectForm} onSubmit={(formEvent) => { formEvent.preventDefault(); void decideProposal(proposal.id, 'REJECTED', rejectionExplanation) }}>
									<InputText label={copy.rejectionReason} name={`proposal-${proposal.id}-rejection`} multiline required value={rejectionExplanation} onChange={setRejectionExplanation} />
									<button className={styles.primaryAction} type="submit" disabled={decisionState === 'submitting' || !rejectionExplanation.trim()}>{copy.rejectChange}</button>
									<button className={styles.secondaryAction} type="button" disabled={decisionState === 'submitting'} onClick={() => setRejectingProposalId(null)}>{copy.cancelChange}</button>
								</form>}
								{mayDecide && decisionState === 'error' && <p className={styles.error} role="alert">{copy.decisionError}</p>}
							</article>
						})}
					</section>}

					{activeTab === 'settings' && mayProposeChanges && <section className={styles.settingsPanel}>
						<h2>{copy.settingsTab}</h2>
						<p>{copy.deleteEventDescription}</p>
						<button className={styles.dangerAction} type="button" disabled={closeState === 'closing'} onClick={() => { if (window.confirm(copy.deleteEventConfirm)) void closeCurrentEvent() }}>{closeState === 'closing' ? copy.deletingEvent : copy.deleteEvent}</button>
						{closeState === 'error' && <p className={styles.error} role="alert">{copy.deleteEventError}</p>}
					</section>}

					{role === 'STAFF' && activeTab === 'checklist' && <section className={styles.tabContent}>
						<h2>{copy.checklistTab}</h2>
						<p className={styles.readOnly}>{copy.staffChecklistEmpty}</p>
					</section>}

					{role === 'STAFF' && activeTab === 'resources' && <section className={styles.tabContent}>
						<h2>{copy.resourcesTab}</h2>
						<p className={styles.readOnly}>{copy.staffResourcesEmpty}</p>
					</section>}
				</section>
			</PageContainer>
		</main>
	)
}

function TabButton({ activeTab, tab, onSelect, children }: { activeTab: EventTab; tab: EventTab; onSelect: (tab: EventTab) => void; children: string }) {
	const active = activeTab === tab
	return <button type="button" className={active ? styles.activeTab : styles.tab} aria-pressed={active} onClick={() => onSelect(tab)}>{children}</button>
}

function Fact({ label, value }: { label: string; value: string }) {
	return <><dt>{label}</dt><dd>{value}</dd></>
}
