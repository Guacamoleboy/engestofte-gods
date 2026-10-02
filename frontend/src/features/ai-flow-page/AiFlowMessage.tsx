// Pathing
// _______
// src/features/ai-flow-page/AiFlowMessage.tsx

import { useEffect, useRef } from 'react'
import { useAiMessageTyping } from './AiFlowPage.hooks'
import styles from './AiFlowPage.module.css'

type AiFlowMessageProps = {
	text: string
	animate: boolean
}

export default function AiFlowMessage({ text, animate }: AiFlowMessageProps) {
	const visibleText = useAiMessageTyping(text, animate)
	const messageRef = useRef<HTMLParagraphElement>(null)

	useEffect(() => {
		const messageList = messageRef.current?.closest<HTMLElement>('[role="log"]')
		if (messageList) messageList.scrollTop = messageList.scrollHeight
	}, [visibleText])

	return <p ref={messageRef} className={`${styles.messageBubble} ${styles.assistantBubble}`}>{visibleText}</p>
}
