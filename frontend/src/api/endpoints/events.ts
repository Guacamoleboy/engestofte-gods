// Pathing
// _______
// src/api/endpoints/events.ts

import { client } from '../client'

export type EventConversationTurn = {
	question: string
	answer: string
}

export type CustomerEvent = {
	event_id: number
	category: 'WEDDING'
	status: 'APPROVED' | 'FOLLOW_UP_REQUIRED' | 'OWNER_FOLLOW_UP_REQUIRED' | 'CLOSED_BY_CUSTOMER' | 'CLOSED_BY_OWNER' | 'AWAITING_APPROVAL' | 'AWAITING_DEPOSIT' | 'BOOKED' | 'CANCELLED_BY_CUSTOMER'
	approved_at: string | null
	event_data: {
		customer_name?: string
		expected_guest_count?: number
		conversation: EventConversationTurn[]
	}
	customer_note: string | null
	created_at: string
}

export type EventMessage = {
	id: number
	sender_type: 'OWNER' | 'CUSTOMER'
	sender_name: string
	content: string
	created_at: string
}

type ApiEnvelope<T> = { data: T }

export async function getEvent(id: number) {
	const response = await client<ApiEnvelope<CustomerEvent>>(`/events/${id}`)
	return response.data
}

export async function getEventMessages(id: number) {
	const response = await client<ApiEnvelope<EventMessage[]>>(`/events/${id}/messages`)
	return response.data
}

export async function sendEventMessage(id: number, content: string) {
	const response = await client<ApiEnvelope<EventMessage>>(`/events/${id}/messages`, {
		method: 'POST',
		body: JSON.stringify({ content }),
	})
	return response.data
}

export async function closeEvent(id: number) {
	const response = await client<ApiEnvelope<CustomerEvent>>(`/events/${id}/close`, { method: 'POST' })
	return response.data
}
