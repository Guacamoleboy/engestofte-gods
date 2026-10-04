// Pathing
// _______
// src/features/event-page/EventPage.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useEventPage } from './EventPage.hooks'
import styles from './EventPage.module.css'

export default function EventPage() {
	const { content: copy, language } = useTranslate()
	const { closeRequest, closeState, event, loadEvent, message, messageState, messages, sendMessage, setMessage, state } = useEventPage()

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.eventPage.loading}</p></PageContainer></main>
	if (state !== 'loaded' || !event) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{state === 'unauthenticated' ? copy.eventPage.unauthenticated : copy.eventPage.error}</p><button type="button" onClick={() => void loadEvent()}>{copy.eventPage.retry}</button></section></PageContainer></main>
	}

	const eventData = event.event_data
	const canReply = event.status === 'FOLLOW_UP_REQUIRED' || event.status === 'APPROVED'
	const canClose = event.status === 'FOLLOW_UP_REQUIRED' && event.approved_at === null
	const statusText = event.status === 'FOLLOW_UP_REQUIRED'
		? copy.eventPage.statusFollowUpRequired
		: event.status === 'OWNER_FOLLOW_UP_REQUIRED'
			? copy.eventPage.statusOwnerFollowUpRequired
			: event.status === 'CLOSED_BY_CUSTOMER' || event.status === 'CLOSED_BY_OWNER'
				? copy.eventPage.statusClosed
				: copy.eventPage.statusApproved

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to="/dashboard/events/">â† {copy.eventPage.back}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{copy.eventPage.eyebrow} · {statusText}</p>
					<h1>{eventData.customer_name || copy.eventPage.title}</h1>
					<p>{copy.eventPage.description}</p>
					<div className={styles.facts}>
						{eventData.expected_guest_count != null && <p className={styles.fact}><span>{copy.eventPage.guestCount}</span>{eventData.expected_guest_count}</p>}
						<p className={styles.fact}><span>{event.approved_at ? copy.eventPage.approvedAt : copy.eventPage.receivedAt}</span><time dateTime={event.approved_at ?? event.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'long' }).format(new Date(event.approved_at ?? event.created_at))}</time></p>
					</div>
				</header>

				<div className={styles.grid}>
					<section className={styles.panel}>
						<h2>{copy.eventPage.detailsTitle}</h2>
						{eventData.conversation.map((turn, index) => (
							<article className={styles.answer} key={`${index}-${turn.question}`}>
								<h3>{turn.question}</h3>
								<p>{turn.answer}</p>
							</article>
						))}
						<section className={styles.noteSection}>
							<h2>{copy.eventPage.noteTitle}</h2>
							{event.customer_note ? <p className={styles.note}>{event.customer_note}</p> : <p>{copy.eventPage.noNote}</p>}
						</section>
					</section>
					<section className={`${styles.panel} ${styles.conversationPanel}`}>
						<h2>{copy.eventPage.conversationTitle}</h2>
						<div className={styles.messages} aria-live="polite">
							{messages.map((item) => (
								<article className={`${styles.message} ${item.sender_type === 'CUSTOMER' ? styles.ownMessage : ''}`} key={item.id}>
									<p className={styles.messageMeta}><strong>{item.sender_type === 'OWNER' ? copy.eventPage.ownerLabel : copy.eventPage.customerLabel}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p>
									<p>{item.content}</p>
								</article>
							))}
							{messages.length === 0 && <p>{copy.eventPage.noMessages}</p>}
						</div>
						{canReply ? (
							<form className={styles.composer} onSubmit={sendMessage}>
								<label htmlFor="event-message">{copy.eventPage.messageLabel}</label>
								<textarea id="event-message" value={message} maxLength={5000} onChange={(event) => setMessage(event.currentTarget.value)} />
								{messageState === 'error' && <p className={styles.error} role="alert">{copy.eventPage.messageError}</p>}
								<button type="submit" disabled={messageState === 'sending' || !message.trim()}>{messageState === 'sending' ? copy.eventPage.sending : copy.eventPage.sendMessage}</button>
							</form>
						) : event.status === 'OWNER_FOLLOW_UP_REQUIRED'
							? <p className={styles.waiting}>{copy.eventPage.waitingForOwner}</p>
							: <p className={styles.waiting}>{copy.eventPage.statusClosed}</p>}
						{canClose && <div className={styles.closeAction}><button type="button" onClick={() => { if (window.confirm(copy.eventPage.closeConfirm)) void closeRequest() }} disabled={closeState === 'closing'}>{closeState === 'closing' ? copy.eventPage.closing : copy.eventPage.closeRequest}</button>{closeState === 'error' && <p className={styles.error} role="alert">{copy.eventPage.closeError}</p>}</div>}
					</section>
				</div>
			</PageContainer>
		</main>
	)
}
