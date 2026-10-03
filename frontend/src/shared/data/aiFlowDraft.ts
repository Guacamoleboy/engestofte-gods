// Pathing
// _______
// src/shared/data/aiFlowDraft.ts

export const AI_FLOW_DRAFT = {
	key: 'engestofte.ai-flow.draft',
	version: 1,
} as const

export type AiFlowDraftStatus = 'none' | 'saved' | 'complete' | 'unreadable'

export function getAiFlowDraftStatus(): AiFlowDraftStatus {
	try {
		const storedValue = window.localStorage.getItem(AI_FLOW_DRAFT.key)
		if (storedValue === null) return 'none'

		const draft: unknown = JSON.parse(storedValue)
		if (!draft || typeof draft !== 'object' || !('version' in draft) || draft.version !== AI_FLOW_DRAFT.version) return 'unreadable'
		if (!('isComplete' in draft) || typeof draft.isComplete !== 'boolean' || !('messages' in draft) || !Array.isArray(draft.messages)) return 'unreadable'
		return draft.isComplete ? 'complete' : 'saved'
	} catch {
		return 'unreadable'
	}
}
