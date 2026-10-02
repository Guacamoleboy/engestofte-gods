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
	status: 'IN_PROGRESS' | 'DONE'
}

export async function submitAiAnswer(answer: string, currentQuestion: string, customerName: string, step: number, language: Language): Promise<AiInteractionResponse> {
	return create<AiInteractionResponse>('ai-flow/interaction', {
		answer,
		current_question: currentQuestion,
		customer_name: customerName,
		step,
		language,
	})
}
