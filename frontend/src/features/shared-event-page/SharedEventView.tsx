// Pathing
// _______
// src/features/shared-event-page/SharedEventView.tsx

import { Link } from 'react-router-dom'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useSharedEventView } from './SharedEventView.hooks'
import styles from './SharedEventView.module.css'

export default function SharedEventView() {
	const { content, language } = useTranslate()
	const copy = content.ownerEvent
	const { addContact, contactEmail, contactState, event, isPrimaryContact, loadEvent, message, messageState, messages, messagesContainerRef, role, sendMessage, setContactEmail, setMessage, state } = useSharedEventView()

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.loading}</p></PageContainer></main>
	if (state !== 'loaded' || !event) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{copy.error}</p><button type="button" onClick={() => void loadEvent()}>{copy.retry}</button></section></PageContainer></main>
	}

	const backPath = role === 'OWNER' ? '/owner/requests' : role === 'STAFF' ? '/staff/events' : '/dashboard/events/'
	const mayMessage = role === 'OWNER' || role === 'CUSTOMER'

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to={backPath}>← {copy.back}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{copy.eyebrow} · {copy.statusApproved}</p>
					<h1>{event.customerName || `${copy.title} #${event.eventId}`}</h1>
					<p>{copy.description}</p>
				</header>

				<div className={styles.grid}>
					<section className={styles.panel}>
						<h2>{copy.detailsTitle}</h2>
						<dl className={styles.facts}>
							{event.customerName && <Fact label={copy.customerName} value={event.customerName} />}
							{event.customerEmail && <><dt>{copy.customerEmail}</dt><dd>{event.customerEmail}</dd></>}
							{event.expectedGuestCount != null && <Fact label={copy.guestCount} value={String(event.expectedGuestCount)} />}
							{event.requestedDate && <Fact label={copy.requestedDate} value={event.requestedDate} />}
							<Fact label={copy.approvedAt} value={new Intl.DateTimeFormat(language, { dateStyle: 'long', timeStyle: 'short' }).format(new Date(event.approvedAt))} />
						</dl>
						{(role === 'OWNER' || (role === 'CUSTOMER' && isPrimaryContact)) && <form className={styles.contactForm} onSubmit={addContact}>
							<label htmlFor="event-contact-email">{copy.contactEmailLabel}</label>
							<input id="event-contact-email" type="email" required value={contactEmail} onChange={(formEvent) => setContactEmail(formEvent.currentTarget.value)} />
							<button type="submit" disabled={contactState === 'adding' || !contactEmail.trim()}>{contactState === 'adding' ? copy.contactAdding : copy.addContact}</button>
							{contactState === 'added' && <p role="status">{copy.contactAdded}</p>}
							{contactState === 'error' && <p className={styles.error} role="alert">{copy.contactError}</p>}
						</form>}
					</section>

					<section className={`${styles.panel} ${styles.conversationPanel}`}>
						<h2>{copy.conversationTitle}</h2>
						{mayMessage ? <>
							<div className={styles.messages} aria-live="polite" ref={messagesContainerRef}>
								{messages.map((item) => (
									<article className={`${styles.message} ${item.sender_type === 'OWNER' ? styles.ownerMessage : styles.customerMessage}`} key={item.id}>
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
					</section>
				</div>
			</PageContainer>
		</main>
	)
}

function Fact({ label, value }: { label: string; value: string }) {
	return <><dt>{label}</dt><dd>{value}</dd></>
}
