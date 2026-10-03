// Pathing
// _______
// src/features/ai-flow-page/AiFlowPage.tsx

import { Link } from 'react-router-dom'
import { useTranslate } from '../../shared/hooks/useTranslate'
import AiFlowMessage from './AiFlowMessage'
import { useAiFlow } from './AiFlowPage.hooks'
import styles from './AiFlowPage.module.css'

export default function AiFlowPage() {
	const { content, language } = useTranslate()
	const copy = content.aiFlow
	const { answer, currentStep, error, formatMessageTime, isAiUnavailable, isComplete, isOutOfScope, isPending, messages, messagesEndRef, setAnswer, submitAnswer } = useAiFlow(copy.introMessage, copy.stepQuestions, copy.finished, copy.customerName, copy.aiUnavailable, language)

	return (
		<main className={`page-container ${styles.page}`}>
			<section className={styles.messengerFrame} aria-labelledby="ai-flow-messenger-title">
				<div className={styles.messengerHeader}>
					<div className={styles.assistantAvatar} aria-hidden="true">
						<img src="/images/shared/logo-white.png" alt="" />
					</div>
					<div className={styles.messengerHeading}>
						<h2 id="ai-flow-messenger-title">{copy.assistantName}</h2>
					</div>
					<p className={styles.flowProgress}>{copy.flowProgress.replace('{step}', String(currentStep))}</p>
				</div>

				<div className={styles.messageList} role="log" aria-live="polite" aria-relevant="additions text" aria-busy={isPending}>
					{messages.map((message) => {
						const isAssistant = message.sender === 'assistant'
						const senderName = isAssistant ? copy.assistantName : message.senderName ?? copy.customerName
						const statusLabel = {
							sending: copy.statusSending,
							sent: copy.statusSent,
							received: copy.statusReceived,
							new: copy.statusNew,
							failed: copy.statusFailed,
						}[message.status]

						return (
							<div className={styles.messageGroup} key={message.id}>
								{message.flowChanged && (
									<div className={styles.flowDivider} role="separator">
										<span>{copy.flowProgress.replace('{step}', String(message.step ?? currentStep))}</span>
									</div>
								)}
								<article className={`${styles.messageRow} ${isAssistant ? styles.assistantRow : styles.customerRow}`}>
									{isAssistant && (
										<div className={styles.messageAvatar} aria-hidden="true">
											<img src="/images/shared/logo-white.png" alt="" />
										</div>
									)}
									<div className={`${styles.messageContent} ${message.id === 0 ? styles.introMessageContent : ''}`}>
										<div className={styles.messageMeta}>
											<strong>{senderName}</strong>
											<time dateTime={message.createdAt.toISOString()}>{formatMessageTime(message.createdAt)}</time>
										</div>
										{isAssistant
											? <>
												<AiFlowMessage text={message.text} animate={message.animate ?? false} />
												{message.id === 0 && (
													<div className={styles.introContactLinks}>
														<Link to="/kontakt">{copy.contactViaWebsite}</Link>
														<a href="mailto:mail@engestofte.dk">{copy.emailUs}</a>
													</div>
												)}
											</>
											: <p className={`${styles.messageBubble} ${styles.customerBubble}`}>{message.text}</p>}
										<p className={`${styles.messageStatus} ${message.status === 'failed' ? styles.failedStatus : ''}`}>
											{message.status === 'sending' && <span className={styles.statusSpinner} aria-hidden="true" />}
											{statusLabel}
										</p>
									</div>
									{!isAssistant && (
										<div className={styles.customerAvatar} aria-hidden="true">
											<svg viewBox="0 0 24 24"><circle cx="12" cy="8" r="3.3" /><path d="M5.5 20c.4-3.5 2.7-5.6 6.5-5.6s6.1 2.1 6.5 5.6" /></svg>
										</div>
									)}
								</article>
							</div>
						)
					})}
					{isPending && (
						<div className={`${styles.messageRow} ${styles.assistantRow}`}>
							<div className={styles.messageAvatar} aria-hidden="true">
								<img src="/images/shared/logo-white.png" alt="" />
							</div>
							<div className={`${styles.messageBubble} ${styles.assistantBubble} ${styles.typingIndicator}`} role="status" aria-label={copy.pending}>
								<span /><span /><span />
							</div>
						</div>
					)}
					<div ref={messagesEndRef} />
				</div>

				{error && <p className={styles.error} role="alert">{copy.error}</p>}

				{isOutOfScope ? (
					<div className={styles.composer}>
						<Link className={styles.contactButton} to="/kontakt">{copy.outOfScopeContact}</Link>
					</div>
				) : isAiUnavailable ? (
					<div className={styles.composer}>
						<Link className={styles.cancelLink} to="/kontakt">{copy.goBack}</Link>
					</div>
				) : !isComplete && !isAiUnavailable && (
				<form className={styles.composer} onSubmit={submitAnswer}>
					<label className="visually-hidden" htmlFor="ai-answer">{copy.answerLabel}</label>
					<textarea
						id="ai-answer"
						value={answer}
						onChange={(event) => setAnswer(event.currentTarget.value)}
						placeholder={copy.answerPlaceholder}
						maxLength={2000}
						rows={2}
						disabled={isPending}
						required
					/>
					<div className={styles.composerActions}>
						<button className={styles.sendButton} type="submit" disabled={isPending || !answer.trim()} aria-label={isPending ? copy.pending : copy.submit}>
							{isPending ? <span className={styles.buttonSpinner} aria-hidden="true" /> : (
								<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4.5 19.5 21 12 4.5 4.5l1.8 6.1L15 12l-8.7 1.4-1.8 6.1Z" /></svg>
							)}
						</button>
						<Link className={styles.cancelLink} to="/kontakt">{copy.cancelRequest}</Link>
					</div>
				</form>
				)}
			</section>
		</main>
	)
}
