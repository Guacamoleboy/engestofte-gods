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
		event_name?: string
		expected_guest_count?: number
		requested_date?: string
		has_allergies?: boolean
		allergy_details?: string
		expected_vegan_count?: number
		wedding_direction?: number
	}
	customer_note: string | null
	created_at: string
	customer_email_redacted: string | null
	is_primary_contact: boolean
	primary_contact_name: string | null
}

export type OwnerEvent = {
	event_id: number
	status: 'APPROVED' | 'AWAITING_APPROVAL' | 'CLOSED_BY_CUSTOMER' | 'CLOSED_BY_OWNER' | 'CANCELLED_BY_CUSTOMER'
	approved_at: string
	customer_name: string
	event_name: string | null
	primary_contact_name: string
	customer_email_redacted: string
	expected_guest_count: number | null
	requested_date: string | null
	has_allergies: boolean | null
	allergy_details: string | null
	expected_vegan_count: number | null
	wedding_direction: number | null
	created_at: string
}

export type StaffEvent = {
	event_id: number
	category: 'WEDDING'
	status: 'APPROVED' | 'FOLLOW_UP_REQUIRED' | 'OWNER_FOLLOW_UP_REQUIRED' | 'AWAITING_APPROVAL' | 'CLOSED_BY_CUSTOMER' | 'CLOSED_BY_OWNER' | 'CANCELLED_BY_CUSTOMER' | 'AWAITING_DEPOSIT' | 'BOOKED'
	approved_at: string
	customer_name: string | null
	event_name: string | null
	expected_guest_count: number | null
	requested_date: string | null
	has_allergies: boolean | null
	allergy_details: string | null
	expected_vegan_count: number | null
	wedding_direction: number | null
	created_at: string
}

export type EventMessage = {
	id: number
	sender_type: 'OWNER' | 'CUSTOMER' | 'STAFF'
	sender_name: string
	is_mine: boolean
	content: string
	created_at: string
}

export type ImportantMessage = {
	event_id: number
	event_name: string
	unread_count: number
	latest_message: string
	latest_message_at: string
}

export type ChangeProposal = {
	id: number
	field_name: string
	old_value: string
	new_value: string
	proposer_name: string
	proposer_party: 'CUSTOMER' | 'OWNER'
	status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUPERSEDED'
	customer_approved: boolean
	owner_approved: boolean
	rejection_explanation: string | null
	created_at: string
}

type ApiEnvelope<T> = { data: T }

export async function getImportantMessages() {
	const response = await client<ApiEnvelope<ImportantMessage[]>>('/events/important-messages')
	return response.data
}

export async function getOwnerImportantMessages() {
	const response = await client<ApiEnvelope<ImportantMessage[]>>('/events/owner/important-messages')
	return response.data
}

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

export async function addEventContact(id: number, email: string) {
	const response = await client<ApiEnvelope<null>>(`/events/${id}/contacts`, { method: 'POST', body: JSON.stringify({ email }) })
	return response.data
}

export async function closeEvent(id: number) {
	const response = await client<ApiEnvelope<CustomerEvent>>(`/events/${id}/close`, { method: 'POST' })
	return response.data
}

export async function getOwnerEvent(id: number) {
	const response = await client<ApiEnvelope<OwnerEvent>>(`/events/owner/${id}`)
	return response.data
}

export async function getStaffEvent(id: number) {
	const response = await client<ApiEnvelope<StaffEvent>>(`/events/staff/${id}`)
	return response.data
}

export async function getStaffEvents() {
	const response = await client<ApiEnvelope<StaffEvent[]>>('/events/staff')
	return response.data
}

export async function getStaffImportantMessages() {
	const response = await client<ApiEnvelope<ImportantMessage[]>>('/events/staff/important-messages')
	return response.data
}

export async function sendStaffEventMessage(id: number, content: string) {
	const response = await client<ApiEnvelope<EventMessage>>(`/events/staff/${id}/messages`, {
		method: 'POST',
		body: JSON.stringify({ content }),
	})
	return response.data
}

export async function getOwnerEventMessages(id: number) {
	const response = await client<ApiEnvelope<EventMessage[]>>(`/events/owner/${id}/messages`)
	return response.data
}

export async function sendOwnerEventMessage(id: number, content: string) {
	const response = await client<ApiEnvelope<EventMessage>>(`/events/owner/${id}/messages`, {
		method: 'POST',
		body: JSON.stringify({ content }),
	})
	return response.data
}

export async function addOwnerEventContact(id: number, email: string) {
	const response = await client<ApiEnvelope<null>>(`/events/owner/${id}/contacts`, { method: 'POST', body: JSON.stringify({ email }) })
	return response.data
}

export async function closeOwnerEvent(id: number) {
	await client<ApiEnvelope<null>>(`/events/owner/${id}/close`, { method: 'POST' })
}

export async function getEventChangeProposals(id: number) {
	const response = await client<ApiEnvelope<ChangeProposal[]>>(`/events/${id}/change-proposals`)
	return response.data
}

export async function getOwnerEventChangeProposals(id: number) {
	const response = await client<ApiEnvelope<ChangeProposal[]>>(`/events/owner/${id}/change-proposals`)
	return response.data
}

export async function proposeEventChange(id: number, fieldName: string, newValue: string) {
	const response = await client<ApiEnvelope<ChangeProposal>>(`/events/${id}/change-proposals`, {
		method: 'POST',
		body: JSON.stringify({ field_name: fieldName, new_value: newValue }),
	})
	return response.data
}

export async function proposeOwnerEventChange(id: number, fieldName: string, newValue: string) {
	const response = await client<ApiEnvelope<ChangeProposal>>(`/events/owner/${id}/change-proposals`, {
		method: 'POST',
		body: JSON.stringify({ field_name: fieldName, new_value: newValue }),
	})
	return response.data
}

export async function decideEventChange(id: number, proposalId: number, decision: 'APPROVED' | 'REJECTED', explanation?: string) {
	const response = await client<ApiEnvelope<ChangeProposal>>(`/events/${id}/change-proposals/${proposalId}/decision`, {
		method: 'POST',
		body: JSON.stringify({ decision, explanation }),
	})
	return response.data
}

export async function decideOwnerEventChange(id: number, proposalId: number, decision: 'APPROVED' | 'REJECTED', explanation?: string) {
	const response = await client<ApiEnvelope<ChangeProposal>>(`/events/owner/${id}/change-proposals/${proposalId}/decision`, {
		method: 'POST',
		body: JSON.stringify({ decision, explanation }),
	})
	return response.data
}
