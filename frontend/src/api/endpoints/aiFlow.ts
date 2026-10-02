// Pathing
// _______
// src/api/endpoints/aiFlow.ts

import { create } from '../crud'
import type { Language } from '../../shared/data/i18n/types'

export type AiInteractionResponse = {
	acknowledgement: string
	next_question: string
	customer_name: string
	step: number
	status: 'IN_PROGRESS' | 'STEP_COMPLETE' | 'OUT_OF_SCOPE' | 'DONE'
}

type AiInteractionEnvelope = {
	data: AiInteractionResponse
}

export type AiConversationTurn = {
	question: string
	answer: string
}

export async function submitAiAnswer(answer: string, currentQuestion: string, customerName: string, step: number, language: Language, conversation: AiConversationTurn[]): Promise<AiInteractionResponse> {
	const response = await create<AiInteractionEnvelope>('ai-flow/interaction', {
		answer,
		current_question: currentQuestion,
		customer_name: customerName,
		step,
		language,
		conversation,
	})
	return response.data
}
