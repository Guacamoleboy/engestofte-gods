// Pathing
// _______
// src/features/ai-flow-page/AiFlowPage.hooks.ts

import { useEffect, useRef, useState, type FormEvent } from 'react'
import { submitAiAnswer, type AiConversationTurn, type AiInteractionResponse } from '../../api/endpoints/aiFlow'
import { ApiError } from '../../api/client'
import { AI_FLOW_DRAFT } from '../../shared/data/aiFlowDraft'
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

const DRAFT_KEY = AI_FLOW_DRAFT.key
const DRAFT_VERSION = AI_FLOW_DRAFT.version

type StoredDraft = {
	version: number
	answer: string
	conversation: AiConversationTurn[]
	currentStep: number
	customerName: string
	expectedGuestCount: number | null
	isComplete: boolean
	isOutOfScope: boolean
	isAiUnavailable: boolean
	messages: (Omit<Message, 'createdAt'> & { createdAt: string })[]
	question: string
	language: Language
	savedAt: string
	submissionId?: string
}

function readDraft(): StoredDraft | null {
	try {
		const value: unknown = JSON.parse(window.localStorage.getItem(DRAFT_KEY) ?? 'null')
		if (!value || typeof value !== 'object' || !('version' in value) || value.version !== DRAFT_VERSION) return null
		const draft = value as StoredDraft
		if (
			!Array.isArray(draft.messages)
			|| !draft.messages.every((message) => message && typeof message.id === 'number' && typeof message.text === 'string' && typeof message.createdAt === 'string' && !Number.isNaN(Date.parse(message.createdAt)))
			|| !Array.isArray(draft.conversation)
			|| !draft.conversation.every((turn) => turn && typeof turn.question === 'string' && typeof turn.answer === 'string')
			|| typeof draft.currentStep !== 'number'
			|| typeof draft.question !== 'string'
			|| typeof draft.answer !== 'string'
			|| !['da', 'en', 'de'].includes(draft.language)
		) return null
		return draft
	} catch {
		return null
	}
}

export function useAiFlow(introMessage: string, stepQuestions: string[], finalMessage: string, defaultCustomerName: string, aiUnavailableMessage: string, language: Language) {
	const [initialDraft] = useState(readDraft)
	const [hasUnrecognizedDraft] = useState(() => {
		try {
			return window.localStorage.getItem(DRAFT_KEY) !== null && initialDraft === null
		} catch {
			return true
		}
	})
	const nextMessageId = useRef(initialDraft ? Math.max(1, ...initialDraft.messages.map((message) => message.id)) + 1 : 2)
	const customerName = useRef(initialDraft?.customerName ?? '')
	const conversation = useRef<AiConversationTurn[]>(initialDraft?.conversation ?? [])
	const messagesEndRef = useRef<HTMLDivElement>(null)
	const [messages, setMessages] = useState<Message[]>(() => initialDraft
		? initialDraft.messages.map((message) => ({
			...message,
			createdAt: new Date(message.createdAt),
			status: message.status === 'sending' ? 'failed' : message.status,
		}))
		: [
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
			text: stepQuestions[0],
			animate: true,
			createdAt: new Date(),
			status: 'new',
		},
	])
	const [question, setQuestion] = useState(initialDraft?.question ?? stepQuestions[0])
	const [currentStep, setCurrentStep] = useState(initialDraft?.currentStep ?? 1)
	const [expectedGuestCount, setExpectedGuestCount] = useState<number | null>(initialDraft?.expectedGuestCount ?? null)
	const [answer, setAnswer] = useState(initialDraft?.answer ?? '')
	const [isPending, setIsPending] = useState(false)
	const [isComplete, setIsComplete] = useState(initialDraft?.isComplete ?? false)
	const [isOutOfScope, setIsOutOfScope] = useState(initialDraft?.isOutOfScope ?? false)
	const [isAiUnavailable, setIsAiUnavailable] = useState(initialDraft?.isAiUnavailable ?? false)
	const [error, setError] = useState('')
	const [hasResumedDraft] = useState(Boolean(initialDraft))

	useEffect(() => {
		if (hasUnrecognizedDraft) return
		const draft: StoredDraft = {
			version: DRAFT_VERSION,
			answer,
			conversation: conversation.current,
			currentStep,
			customerName: customerName.current,
			expectedGuestCount,
			isComplete,
			isOutOfScope,
			isAiUnavailable,
			messages: messages.map((message) => ({ ...message, createdAt: message.createdAt.toISOString() })),
			question,
			language,
			savedAt: new Date().toISOString(),
			submissionId: initialDraft?.submissionId,
		}
		window.localStorage.setItem(DRAFT_KEY, JSON.stringify(draft))
	}, [answer, currentStep, expectedGuestCount, hasUnrecognizedDraft, isAiUnavailable, isComplete, isOutOfScope, language, messages, question])

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
		if (!submittedAnswer || isPending || isComplete || isOutOfScope || isAiUnavailable) return

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
		const submittedTurn = { question, answer: submittedAnswer }
		try {
			const result: AiInteractionResponse = await submitAiAnswer(
				submittedAnswer,
				question,
				customerName.current,
				currentStep,
				language,
				[...conversation.current, submittedTurn],
			)
			if (result.customer_name) customerName.current = result.customer_name
			conversation.current = [...conversation.current, submittedTurn]
			const guestCount = findExpectedGuestCount(conversation.current) ?? result.expected_guest_count
			if (typeof guestCount === 'number' && guestCount > 0) setExpectedGuestCount(guestCount)

			let nextStep = currentStep
			let nextQuestion = ''
			if (result.status === 'STEP_COMPLETE') {
				nextStep = currentStep + 1
				nextQuestion = stepQuestions[nextStep - 1].replace('{name}', customerName.current || defaultCustomerName)
			} else if (result.status === 'DONE') {
				nextStep = 6
				setIsComplete(true)
				nextQuestion = finalMessage.replace('{name}', customerName.current || defaultCustomerName)
			} else if (result.status === 'OUT_OF_SCOPE') {
				setIsOutOfScope(true)
			} else {
				nextQuestion = result.next_question
			}

			const flowChanged = nextStep > currentStep
			setCurrentStep(nextStep)
			const acknowledgement = result.acknowledgement.trimEnd()
			const followUp = nextQuestion.trim()
			const acknowledgementAlreadyAsksFollowUp = result.status === 'IN_PROGRESS'
				&& followUp.length > 0
				&& acknowledgement.toLocaleLowerCase(language).endsWith(followUp.toLocaleLowerCase(language))
			const dateAcknowledgementRepeatsUnavailableYear = currentStep === 2
				&& result.status === 'IN_PROGRESS'
				&& /\b2027\b/.test(submittedAnswer)
			const responseAcknowledgement = dateAcknowledgementRepeatsUnavailableYear
				? ''
				: currentStep === 1 && result.status === 'STEP_COMPLETE'
					? ''
				: acknowledgementAlreadyAsksFollowUp
					? acknowledgement.slice(0, -followUp.length).trimEnd()
					: acknowledgement
			const assistantText = [responseAcknowledgement, nextQuestion].filter(Boolean).join('\n\n')
			setMessages((currentMessages) => [...currentMessages.map((message): Message =>
				message.id === outgoingId ? { ...message, senderName: customerName.current || senderName, status: 'sent' } : message,
			), {
				id: nextMessageId.current++,
				sender: 'assistant',
				flowChanged,
				step: nextStep,
				animate: true,
				senderName: customerName.current || defaultCustomerName,
				text: assistantText,
				createdAt: new Date(),
				status: 'received',
			}])
			if (result.status === 'IN_PROGRESS') setQuestion(result.next_question)
			else if (result.status === 'STEP_COMPLETE') setQuestion(nextQuestion)
		} catch (error) {
			setMessages((currentMessages) => currentMessages.map((message) =>
				message.id === outgoingId ? { ...message, status: 'failed' } : message,
			))
			if (!(error instanceof ApiError) || error.code >= 500) {
				setIsAiUnavailable(true)
				setMessages((currentMessages) => [...currentMessages, {
					id: nextMessageId.current++,
					sender: 'assistant',
					text: aiUnavailableMessage,
					createdAt: new Date(),
					status: 'received',
				}])
			} else {
				setError('request-failed')
			}
		} finally {
			setIsPending(false)
		}
	}

	return { answer, currentStep, customerName: customerName.current || defaultCustomerName, expectedGuestCount, error, formatMessageTime, hasResumedDraft, hasUnrecognizedDraft, isAiUnavailable, isComplete, isOutOfScope, isPending, messages, messagesEndRef, setAnswer, submitAnswer }
}

function findExpectedGuestCount(conversation: AiConversationTurn[]) {
	for (const turn of [...conversation].reverse()) {
		const question = turn.question.toLocaleLowerCase()
		const askedGuestCount = /(gæst|guest|gäste|personen).*(forvent|expect|erwart|rechn)/.test(question)
		const range = turn.answer.match(/\b\d{1,3}\s*(?:-|–|til|to)\s*(\d{1,3})\s*(?:gæster|guest(?:s)?|gäste|personer?)\b/i)
		const explicitCount = turn.answer.match(/\b(\d{1,3})\s*(?:gæster|guest(?:s)?|gäste|personer?)\b/i)
		if (!askedGuestCount && !range && !explicitCount) continue
		const count = range?.[1] ?? explicitCount?.[1] ?? (/^\d{1,3}$/.test(turn.answer.trim()) ? turn.answer.trim() : null)
		if (!count) continue

		const parsedCount = Number(count)
		if (parsedCount >= 1 && parsedCount <= 150) return parsedCount
	}
	return null
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
