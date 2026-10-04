// Pathing
// _______
// src/features/customer-approval-page/CustomerApprovalView.tsx

import { Link } from 'react-router-dom'
import { useState } from 'react'
import PageContainer from '../../shared/components/PageContainer'
import { useTranslate } from '../../shared/hooks/useTranslate'
import { useCustomerApprovalView } from './CustomerApprovalView.hooks'
import styles from './CustomerApprovalView.module.css'

export default function CustomerApprovalView() {
	const { content, language } = useTranslate()
	const copy = content.eventPage
	const { closeRequest, closeState, enquiry, event, loadEvent, message, messageState, messages, sendMessage, setMessage, state } = useCustomerApprovalView()
	const [activeTab, setActiveTab] = useState<'status' | 'messages'>('status')

	if (state === 'loading') return <main className={styles.page}><PageContainer className={styles.content}><p role="status">{copy.loading}</p></PageContainer></main>
	if (state !== 'loaded' || !enquiry) {
		return <main className={styles.page}><PageContainer className={styles.content}><section className={styles.state} role="alert"><p>{state === 'unauthenticated' ? copy.unauthenticated : copy.error}</p><button type="button" onClick={() => void loadEvent()}>{copy.retry}</button></section></PageContainer></main>
	}

	const status = event?.approved_at ? 'APPROVED' : enquiry?.status ?? event?.status ?? 'SUBMITTED'
	const isClosed = ['CLOSED_BY_OWNER', 'CLOSED_BY_CUSTOMER', 'CANCELLED_BY_CUSTOMER'].includes(status)
	const canReply = event !== null && !isClosed && event.approved_at === null
	const canClose = !['APPROVED', 'CLOSED_BY_OWNER', 'CLOSED_BY_CUSTOMER', 'CANCELLED_BY_CUSTOMER'].includes(status)
	const statusText = status === 'FOLLOW_UP_REQUIRED'
		? copy.statusFollowUpRequired
		: status === 'OWNER_FOLLOW_UP_REQUIRED'
			? copy.statusOwnerFollowUpRequired
			: status === 'SUBMITTED' || status === 'UNDER_REVIEW' || status === 'AWAITING_CUSTOMER'
								? copy.statusPending
				: copy.statusClosed

	return (
		<main className={styles.page}>
			<PageContainer className={styles.content}>
				<Link className={styles.backLink} to="/dashboard/events/">← {copy.back}</Link>
				<header className={styles.header}>
					<p className={styles.eyebrow}>{content.eventsDashboard.requestTitle} · {statusText}</p>
					<h1>{copy.conversationTitle}</h1>
				</header>

				<section className={`${styles.conversationPanel} ${activeTab === 'status' ? styles.statusPanel : styles.messagesPanel}`}>
					<nav className={styles.tabs} aria-label={copy.conversationTitle}>
						<button type="button" className={activeTab === 'status' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'status'} onClick={() => setActiveTab('status')}>{copy.statusTab}</button>
						<button type="button" className={activeTab === 'messages' ? styles.activeTab : styles.tab} aria-pressed={activeTab === 'messages'} onClick={() => setActiveTab('messages')}>{copy.messagesTab}</button>
					</nav>
					{activeTab === 'status' ? <div className={styles.statusContent}>
						<p className={styles.waiting}>{statusText}</p>
						{canClose && <div className={styles.closeAction}><button type="button" onClick={() => { if (window.confirm(copy.closeConfirm)) void closeRequest() }} disabled={closeState === 'closing'}>{closeState === 'closing' ? copy.closing : copy.closeRequest}</button>{closeState === 'error' && <p className={styles.error} role="alert">{copy.closeError}</p>}</div>}
					</div> : <>
					<div className={styles.messages} aria-live="polite">
						{messages.map((item) => (
							<article className={`${styles.message} ${item.sender_type === 'CUSTOMER' ? styles.ownMessage : ''}`} key={item.id}>
								<p className={styles.messageMeta}><strong>{item.sender_type === 'OWNER' ? copy.ownerLabel : copy.customerLabel}</strong><time dateTime={item.created_at}>{new Intl.DateTimeFormat(language, { dateStyle: 'medium', timeStyle: 'short' }).format(new Date(item.created_at))}</time></p>
								<p>{item.content}</p>
							</article>
						))}
						{messages.length === 0 && <p>{copy.noMessages}</p>}
					</div>
					{canReply ? (
						<form className={styles.composer} onSubmit={sendMessage}>
							<label htmlFor="approval-message">{copy.followUpReplyLabel}</label>
							<textarea id="approval-message" value={message} maxLength={5000} onChange={(formEvent) => setMessage(formEvent.currentTarget.value)} />
							{messageState === 'error' && <p className={styles.error} role="alert">{copy.messageError}</p>}
							<button type="submit" disabled={messageState === 'sending' || !message.trim()}>{messageState === 'sending' ? copy.sending : copy.sendFollowUpReply}</button>
						</form>
					) : status === 'OWNER_FOLLOW_UP_REQUIRED'
						? <p className={styles.waiting}>{copy.waitingForOwner}</p>
						: <p className={styles.waiting}>{isClosed ? copy.statusClosed : copy.statusPending}</p>}
					</>}
				</section>
			</PageContainer>
		</main>
	)
}
