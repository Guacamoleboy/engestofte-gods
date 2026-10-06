// Pathing
// _______
// src/api/endpoints/ownerEnquiries.ts

import { client } from '../client'

export type OwnerEnquiryStatus = 'SUBMITTED' | 'UNDER_REVIEW' | 'AWAITING_CUSTOMER' | 'FOLLOW_UP_REQUIRED' | 'OWNER_FOLLOW_UP_REQUIRED' | 'APPROVED' | 'AWAITING_DEPOSIT' | 'BOOKED' | 'CLOSED_BY_OWNER' | 'CLOSED_BY_CUSTOMER' | 'CANCELLED_BY_CUSTOMER'
export type OpenOwnerEnquiryStatus = Exclude<OwnerEnquiryStatus, 'CLOSED_BY_OWNER' | 'CLOSED_BY_CUSTOMER' | 'CANCELLED_BY_CUSTOMER'>

export type OwnerEnquirySummary = {
	id: number
	customer_name: string
	summary: string
	status: OpenOwnerEnquiryStatus
	event_id: number | null
	event_approved_at: string | null
	submitted_at: string
}

export type EnquiryConversationTurn = {
	question: string
	answer: string
}

export type AiEnquiryAssessment = {
	summary: string
	missing_information: string[]
	uncertainties: string[]
	conflicts: string[]
	upsell_suggestions: string[]
}

export type OwnerEnquiryReview = {
	id: number
	submission_id: string
	language: 'da' | 'en' | 'de'
	status: OwnerEnquiryStatus
	event_id: number | null
	event_approved_at: string | null
	submitted_at: string
	draft: {
		isComplete?: boolean
		customerName?: string
		expectedGuestCount?: number | null
		conversation?: EnquiryConversationTurn[]
	}
	ai_assessment: AiEnquiryAssessment | null
	internal_note: string | null
	customer_question: string | null
}

export type EventApproval = {
	event_id: number
	status: 'APPROVED'
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

export async function getOwnerEnquiries() {
	const response = await client<ApiEnvelope<OwnerEnquirySummary[]>>('/enquiries/owner')
	return response.data
}

export async function getOwnerEnquiry(id: number) {
	const response = await client<ApiEnvelope<OwnerEnquiryReview>>(`/enquiries/owner/${id}`)
	return response.data
}

export async function saveOwnerEnquiryReview(id: number, internalNote: string, customerQuestion: string) {
	const response = await client<ApiEnvelope<OwnerEnquiryReview>>(`/enquiries/owner/${id}`, {
		method: 'PUT',
		body: JSON.stringify({ internal_note: internalNote, customer_question: customerQuestion }),
	})
	return response.data
}

export async function approveOwnerEnquiry(id: number, customerNote: string) {
	const response = await client<ApiEnvelope<EventApproval>>(`/enquiries/owner/${id}/approve`, {
		method: 'POST',
		body: JSON.stringify({ customer_note: customerNote }),
	})
	return response.data
}

export async function getOwnerEnquiryMessages(id: number) {
	const response = await client<ApiEnvelope<EventMessage[]>>(`/enquiries/owner/${id}/messages`)
	return response.data
}

export async function sendOwnerEnquiryMessage(id: number, content: string) {
	const response = await client<ApiEnvelope<EventMessage>>(`/enquiries/owner/${id}/messages`, {
		method: 'POST',
		body: JSON.stringify({ content }),
	})
	return response.data
}

export async function closeOwnerEnquiry(id: number, reason: string) {
	return client<ApiEnvelope<string>>(`/enquiries/owner/${id}/close`, {
		method: 'POST',
		body: JSON.stringify({ content: reason }),
	})
}
