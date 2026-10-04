// Pathing
// _______
// src/api/endpoints/ownerEnquiries.ts

import { client } from '../client'

export type OwnerEnquiryStatus = 'SUBMITTED' | 'UNDER_REVIEW' | 'AWAITING_CUSTOMER'

export type OwnerEnquirySummary = {
	id: number
	customer_name: string
	summary: string
	status: OwnerEnquiryStatus
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
	submitted_at: string
	draft: {
		customerName?: string
		expectedGuestCount?: number | null
		conversation?: EnquiryConversationTurn[]
	}
	ai_assessment: AiEnquiryAssessment | null
	internal_note: string | null
	customer_question: string | null
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
