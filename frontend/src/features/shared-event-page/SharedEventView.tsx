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
	const { addContact, closeCurrentEvent, closeState, contactEmail, contactState, copyInvitationLink, decideProposal, decisionState, depositState, event, hasProposalChanges, invitationCopyState, invitationState, invitationUrl, isPrimaryContact, loadEvent, message, messageState, messages, messagesContainerRef, openInvitation, payDeposit, proposalState, proposalValues, proposals, rejectionExplanation, rejectingProposalId, requestDeposit, role, sendMessage, setContactEmail, setMessage, setProposalValue, setRejectionExplanation, setRejectingProposalId, state, submitEventProposals } = useSharedEventView()
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

	const backPath = role === 'OWNER' ? '/owner/bookings' : role === 'STAFF' ? '/staff/events' : '/dashboard/bookings'
	const isClosed = event.status === 'CLOSED_BY_CUSTOMER' || event.status === 'CLOSED_BY_OWNER' || event.status === 'CANCELLED_BY_CUSTOMER'
	const mayMessage = !isClosed && (role === 'OWNER' || role === 'CUSTOMER' || role === 'STAFF')
	const mayProposeChanges = !isClosed && (role === 'OWNER' || (role === 'CUSTOMER' && isPrimaryContact))
	const mayManageSettings = !isClosed && (role === 'CUSTOMER' ? isPrimaryContact : role === 'OWNER' && event.status !== 'BOOKED')

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to={backPath}>← {copy.back}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{copy.eyebrow} · {event.status === 'AWAITING_APPROVAL' ? copy.statusAwaitingApproval
						: event.status === 'CANCELLED_BY_CUSTOMER' ? copy.statusCancelledByCustomer
							: isClosed ? copy.statusClosed
							: event.status === 'BOOKED' ? copy.statusBooked
								: event.status === 'AWAITING_DEPOSIT' ? copy.statusAwaitingDeposit : copy.statusApproved}</p>
					<h1>{event.eventName || copy.defaultEventName}</h1>
					<p>{copy.description}</p>
				</header>
				{event.status === 'BOOKED' && <section className={styles.bookingNotice} role="status"><p>{copy.bookingConfirmation}</p></section>}
				{role === 'CUSTOMER' && event.status === 'CANCELLED_BY_CUSTOMER' && <section className={styles.bookingNotice} role="status"><p>{copy.cancelledConfirmation}</p></section>}

				<section className={`${styles.panel} ${role === 'STAFF' ? styles.staffPanel : ''}`}>
					<nav className={styles.tabs} aria-label={copy.detailsTitle}>
						<TabButton activeTab={activeTab} tab="details" onSelect={setActiveTab}>{copy.detailsTab}</TabButton>
						<TabButton activeTab={activeTab} tab="conversation" onSelect={setActiveTab}>{role === 'STAFF' ? copy.staffSendMessage : copy.conversationTab}</TabButton>
						{role !== 'STAFF' && <TabButton activeTab={activeTab} tab="important" onSelect={setActiveTab}>{copy.importantTab}</TabButton>}
						{mayManageSettings && <TabButton activeTab={activeTab} tab="settings" onSelect={setActiveTab}>{copy.settingsTab}</TabButton>}
						{role === 'OWNER' && <>
							<button className={styles.lockedTab} type="button" disabled>{copy.trelloTab}</button>
							<button className={styles.lockedTab} type="button" disabled>{copy.resourcesTab}</button>
							<button className={styles.lockedTab} type="button" disabled>{copy.checklistTab}</button>
						</>}
						{role === 'STAFF' && <>
							<button className={styles.lockedTab} type="button" disabled>{copy.resourcesTab}</button>
							<button className={styles.lockedTab} type="button" disabled>{copy.checklistTab}</button>
						</>}
					</nav>

					{activeTab === 'details' && <section className={styles.tabContent}>
						<h2>{copy.detailsTitle}</h2>
						<section className={styles.generalDetails}>
							<h3>{copy.generalTitle}</h3>
							<dl className={styles.facts}>
								{event.customerEmail && <Fact label={copy.customerEmail} value={event.customerEmail} />}
								{(event.customerName || event.primaryContactName) && <Fact label={copy.customerName} value={event.customerName || event.primaryContactName || ''} />}
								<Fact label={copy.approvedAt} value={new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(event.approvedAt))} />
							</dl>
						</section>
						<hr className={styles.divider} />
						{role === 'STAFF' ? <section className={styles.specificDetails}>
							<h3>{copy.specificDetailsTitle}</h3>
							<InputText label={copy.eventName} name="staff-event-name" value={event.eventName || ''} placeholder={copy.notProvided} disabled />
							<InputText label={copy.guestCount} name="staff-event-guest-count" value={event.expectedGuestCount == null ? '' : String(event.expectedGuestCount)} placeholder={copy.notProvided} disabled />
							<InputText label={copy.requestedDate} name="staff-event-requested-date" value={event.requestedDate || ''} placeholder={copy.notProvided} disabled />
							<InputText label={copy.expectedVeganCount} name="staff-event-vegan-count" type="number" value={event.expectedVeganCount == null ? '' : String(event.expectedVeganCount)} placeholder={copy.notProvided} disabled />
							<InputText label={copy.allergies} name="staff-event-allergies" value={event.hasAllergies == null ? '' : event.hasAllergies ? copy.yes : copy.no} placeholder={copy.notProvided} disabled />
							{event.hasAllergies && <InputText label={copy.allergyDetails} name="staff-event-allergy-details" value={event.allergyDetails || ''} placeholder={copy.notProvided} disabled />}
							<InputText label={copy.weddingDirection} name="staff-event-wedding-direction" value={weddingDirectionLabel(event.weddingDirection, content.aiFlow.weddingDirectionOptions, '')} placeholder={copy.notProvided} disabled />
						</section> : <form className={styles.specificDetails} onSubmit={(formEvent) => { formEvent.preventDefault(); void submitEventProposals() }}>
							<h3>{copy.specificDetailsTitle}</h3>
							<InputText label={copy.eventName} name="event-name" placeholder={event.eventName || copy.defaultEventName} value={proposalValues.event_name} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('event_name', value)} />
							<InputText label={copy.guestCount} name="event-guest-count" type="number" min={1} max={5000} step={1} placeholder={event.expectedGuestCount == null ? '' : String(event.expectedGuestCount)} value={proposalValues.expected_guest_count} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('expected_guest_count', value)} />
							<InputText label={copy.requestedDate} name="event-requested-date" placeholder={event.requestedDate ?? ''} value={proposalValues.requested_date} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('requested_date', value)} />
							<InputText label={copy.expectedVeganCount} name="event-vegan-count" type="number" min={0} max={event.expectedGuestCount ?? 150} step={1} value={proposalValues.expected_vegan_count} placeholder={event.expectedVeganCount == null ? copy.notProvided : String(event.expectedVeganCount)} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('expected_vegan_count', value)} />
							<InputText label={copy.allergies} name="event-allergies" value={proposalValues.has_allergies} placeholder={event.hasAllergies == null ? copy.notProvided : event.hasAllergies ? copy.yes.toUpperCase() : copy.no.toUpperCase()} options={[{ label: copy.yes.toUpperCase(), value: 'true' }, { label: copy.no.toUpperCase(), value: 'false' }]} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('has_allergies', value)} />
							{(proposalValues.has_allergies === 'true' || (proposalValues.has_allergies === '' && event.hasAllergies)) && <InputText label={copy.allergyDetails} name="event-allergy-details" value={proposalValues.allergy_details} placeholder={event.allergyDetails || copy.notProvided} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('allergy_details', value)} />}
							<InputText label={copy.weddingDirection} name="event-wedding-direction" value={proposalValues.wedding_direction} placeholder={weddingDirectionLabel(event.weddingDirection, content.aiFlow.weddingDirectionOptions, copy.notProvided)} options={content.aiFlow.weddingDirectionOptions.map((option, index) => ({ label: option.title, value: String(index + 1) }))} disabled={!mayProposeChanges} onChange={(value) => setProposalValue('wedding_direction', value)} />
							{proposalState === 'error' && <p className={styles.error} role="alert">{copy.proposalError}</p>}
							{mayProposeChanges && <button className={styles.primaryAction} type="submit" disabled={!hasProposalChanges || proposalState === 'submitting'}>{proposalState === 'submitting' ? copy.proposalSending : copy.proposeChange}</button>}
						</form>}
						{role === 'OWNER' && event.status === 'APPROVED' && !proposals.some((proposal) => proposal.status === 'PENDING') && <section className={styles.bookingActions}>
							<button className={styles.primaryAction} type="button" disabled={depositState === 'sending'} onClick={() => void requestDeposit()}>{depositState === 'sending' ? copy.requestDepositSending : copy.requestDeposit}</button>
							{depositState === 'error' && <p className={styles.error} role="alert">{copy.requestDepositError}</p>}
						</section>}
						{role === 'CUSTOMER' && isPrimaryContact && event.status === 'AWAITING_DEPOSIT' && !proposals.some((proposal) => proposal.status === 'PENDING') && <section className={styles.bookingActions}>
							<p>{copy.depositReady}</p>
							<button className={styles.primaryAction} type="button" disabled={depositState === 'sending'} onClick={() => void payDeposit()}>{depositState === 'sending' ? copy.payDepositSending : copy.payDeposit}</button>
							{depositState === 'error' && <p className={styles.error} role="alert">{copy.payDepositError}</p>}
						</section>}
						{role === 'CUSTOMER' && isPrimaryContact && event.status === 'BOOKED' && <section className={styles.invitationActions}>
							<h3>{copy.invitationTitle}</h3>
							<p>{copy.invitationDescription}</p>
							<button className={styles.primaryAction} type="button" disabled={invitationState === 'opening'} onClick={openInvitation}>
								{invitationState === 'opening' ? copy.invitationOpening : event.guestInvitationCreated ? copy.viewInvitation : copy.createInvitation}
							</button>
							{invitationState === 'error' && <p className={styles.error} role="alert">{copy.invitationError}</p>}
							{invitationUrl && <div className={styles.invitationLink}>
								<label htmlFor="guest-invitation-link">{copy.invitationUrlLabel}</label>
								<div><input id="guest-invitation-link" type="url" readOnly value={invitationUrl} /><button className={styles.secondaryAction} type="button" onClick={() => void copyInvitationLink()}>{copy.copyInvitation}</button></div>
								{invitationCopyState === 'copied' && <p role="status">{copy.invitationCopied}</p>}
								{invitationCopyState === 'error' && <p className={styles.error} role="alert">{copy.invitationCopyError}</p>}
							</div>}
						</section>}
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
						<h2>{role === 'STAFF' ? copy.staffSendMessage : copy.conversationTitle}</h2>
						{mayMessage ? <>
							{role !== 'STAFF' && <div className={styles.messages} aria-live="polite" ref={messagesContainerRef}>
								{messages.map((item) => (
								<article className={`${styles.message} ${item.is_mine ? styles.ownMessage : styles.otherMessage} ${item.sender_type === 'OWNER' || item.sender_type === 'STAFF' ? styles.ownerMessage : styles.customerMessage}`} key={item.id}>
										<p className={styles.messageMeta}><strong>{item.sender_name}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p>
										<p>{item.content}</p>
									</article>
								))}
								{messages.length === 0 && <p>{copy.noMessages}</p>}
							</div>}
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

					{activeTab === 'settings' && mayManageSettings && <section className={styles.settingsPanel}>
						<h2>{copy.settingsTab}</h2>
						<p>{role === 'CUSTOMER' ? copy.cancelEventDescription : copy.deleteEventDescription}</p>
						<button className={styles.dangerAction} type="button" disabled={closeState === 'closing'} onClick={() => {
							const isCustomerCancellationConfirmed = role === 'CUSTOMER'
								? window.confirm(copy.cancelEventConfirm) && window.confirm(copy.cancelEventFinalConfirm)
								: window.confirm(copy.deleteEventConfirm)
							if (isCustomerCancellationConfirmed) void closeCurrentEvent()
						}}>{closeState === 'closing' ? role === 'CUSTOMER' ? copy.cancelEventSending : copy.deletingEvent : role === 'CUSTOMER' ? copy.cancelEvent : copy.deleteEvent}</button>
						{closeState === 'error' && <p className={styles.error} role="alert">{role === 'CUSTOMER' ? copy.cancelEventError : copy.deleteEventError}</p>}
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

function weddingDirectionLabel(direction: number | null, options: { title: string; description: string; choice: string }[], fallback: string) {
	const selectedOption = direction == null ? null : options[direction - 1]
	return selectedOption ? selectedOption.title : fallback
}
