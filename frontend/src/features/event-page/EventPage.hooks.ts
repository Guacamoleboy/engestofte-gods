// Pathing
// _______
// src/features/event-page/EventPage.hooks.ts

import { useCallback, useEffect, useState, type FormEvent } from 'react'
import { useParams } from 'react-router-dom'
import { ApiError } from '../../api/client'
import { closeEvent, getEvent, getEventMessages, sendEventMessage, type CustomerEvent, type EventMessage } from '../../api/endpoints/events'

export function useEventPage() {
	const { id: rawId } = useParams()
	const id = Number(rawId)
	const [event, setEvent] = useState<CustomerEvent | null>(null)
	const [messages, setMessages] = useState<EventMessage[]>([])
	const [message, setMessage] = useState('')
	const [state, setState] = useState<'loading' | 'loaded' | 'error' | 'unauthenticated'>('loading')
	const [messageState, setMessageState] = useState<'idle' | 'sending' | 'error'>('idle')
	const [closeState, setCloseState] = useState<'idle' | 'closing' | 'error'>('idle')

	const loadEvent = useCallback(async () => {
		if (!Number.isInteger(id) || id < 1) {
			setState('error')
			return
		}
		setState('loading')
		try {
			const [loadedEvent, loadedMessages] = await Promise.all([getEvent(id), getEventMessages(id)])
			setEvent(loadedEvent)
			setMessages(loadedMessages)
			setState('loaded')
		} catch (error) {
			setState(error instanceof ApiError && (error.status === 401 || error.status === 403) ? 'unauthenticated' : 'error')
		}
	}, [id])

	useEffect(() => { void loadEvent() }, [loadEvent])

	const sendMessage = useCallback(async (formEvent: FormEvent<HTMLFormElement>) => {
		formEvent.preventDefault()
		if (!message.trim()) return
		setMessageState('sending')
		try {
			const sentMessage = await sendEventMessage(id, message)
			setMessages((current) => [...current, sentMessage])
			setMessage('')
			setMessageState('idle')
			setEvent((current) => current ? { ...current, status: 'OWNER_FOLLOW_UP_REQUIRED' } : current)
		} catch {
			setMessageState('error')
		}
	}, [id, message])

	const closeRequest = useCallback(async () => {
		setCloseState('closing')
		try {
			setEvent(await closeEvent(id))
			setCloseState('idle')
		} catch {
			setCloseState('error')
		}
	}, [id])

	return { closeRequest, closeState, event, loadEvent, message, messageState, messages, sendMessage, setMessage, state }
}
