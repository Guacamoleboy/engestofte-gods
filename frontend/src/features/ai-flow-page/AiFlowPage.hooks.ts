// Pathing
// _______
// src/features/ai-flow-page/AiFlowPage.hooks.ts

import { useEffect, useRef, useState, type FormEvent } from 'react'
import { submitAiAnswer, type AiInteractionResponse } from '../../api/endpoints/aiFlow'
import type { Language } from '../../shared/data/i18n/types'

type Message = {
	id: number
	sender: 'assistant' | 'customer'
	senderName?: string
	text: string
	flowChanged?: boolean
	step?: number
	animate?: boolean
	createdAt: Date
	status: 'sending' | 'sent' | 'received' | 'new' | 'failed'
}

export function useAiFlow(introMessage: string, firstQuestion: string, defaultCustomerName: string, language: Language) {
	const nextMessageId = useRef(2)
	const customerName = useRef('')
	const messagesEndRef = useRef<HTMLDivElement>(null)
	const [messages, setMessages] = useState<Message[]>(() => [
		{
			id: 0,
			sender: 'assistant',
			text: introMessage,
			createdAt: new Date(),
			status: 'new',
		},
		{
			id: 1,
			sender: 'assistant',
			text: firstQuestion,
			animate: true,
			createdAt: new Date(),
			status: 'new',
		},
	])
	const [question, setQuestion] = useState(firstQuestion)
	const [currentStep, setCurrentStep] = useState(1)
	const [answer, setAnswer] = useState('')
	const [isPending, setIsPending] = useState(false)
	const [error, setError] = useState('')

	useEffect(() => {
		messagesEndRef.current?.scrollIntoView({ behavior: 'smooth', block: 'end' })
	}, [messages, isPending])

	function formatMessageTime(date: Date) {
		return new Intl.DateTimeFormat(language, {
			day: 'numeric',
			month: 'short',
			hour: '2-digit',
			minute: '2-digit',
		}).format(date)
	}

	async function submitAnswer(event: FormEvent<HTMLFormElement>) {
		event.preventDefault()
		const submittedAnswer = answer.trim()
		if (!submittedAnswer || isPending) return

		const retryMessage = [...messages].reverse().find((message) =>
			message.sender === 'customer' && message.status === 'failed' && message.text === submittedAnswer,
		)
		const outgoingId = retryMessage?.id ?? nextMessageId.current++
		const senderName = customerName.current || defaultCustomerName
		const sentAt = new Date()
		setMessages((currentMessages) => retryMessage
			? currentMessages.map((message) => message.id === outgoingId
				? { ...message, createdAt: sentAt, status: 'sending' }
				: message)
			: [...currentMessages, {
				id: outgoingId,
				sender: 'customer',
				senderName,
				text: submittedAnswer,
				createdAt: sentAt,
				status: 'sending',
			}])
		setIsPending(true)
		setError('')
		setAnswer('')
		try {
			const result: AiInteractionResponse = await submitAiAnswer(submittedAnswer, question, customerName.current, currentStep, language)
			if (result.customer_name) customerName.current = result.customer_name
			const nextStep = result.status === 'DONE' ? 6 : Math.min(Math.max(result.step, 1), 5)
			const flowChanged = nextStep > currentStep
			setCurrentStep(nextStep)
			setMessages((currentMessages) => [...currentMessages.map((message) =>
				message.id === outgoingId ? { ...message, senderName: customerName.current, status: 'sent' } : message,
			), {
				id: nextMessageId.current++,
				sender: 'assistant',
				flowChanged,
				step: nextStep,
				animate: true,
				senderName: customerName.current,
				text: `${result.acknowledgement}\n\n${result.next_question}`,
				createdAt: new Date(),
				status: 'received',
			}])
			setQuestion(result.next_question)
		} catch {
			setMessages((currentMessages) => currentMessages.map((message) =>
				message.id === outgoingId ? { ...message, status: 'failed' } : message,
			))
			setError('request-failed')
		} finally {
			setIsPending(false)
		}
	}

	return { answer, currentStep, error, formatMessageTime, isPending, messages, messagesEndRef, setAnswer, submitAnswer }
}

export function useAiMessageTyping(text: string, animate: boolean) {
	const [visibleText, setVisibleText] = useState(animate ? '' : text)

	useEffect(() => {
		if (!animate || window.matchMedia('(prefers-reduced-motion: reduce)').matches) {
			setVisibleText(text)
			return
		}

		setVisibleText('')
		const characters = Array.from(text)
		let characterCount = 0
		const intervalId = window.setInterval(() => {
			characterCount += 1
			setVisibleText(characters.slice(0, characterCount).join(''))
			if (characterCount >= characters.length) window.clearInterval(intervalId)
		}, 18)

		return () => window.clearInterval(intervalId)
	}, [animate, text])

	return visibleText
}
