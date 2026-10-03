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

export function getCompleteAiFlowDraft(): Record<string, unknown> | null {
	if (getAiFlowDraftStatus() !== 'complete') return null
	try {
		const draft: unknown = JSON.parse(window.localStorage.getItem(AI_FLOW_DRAFT.key) ?? 'null')
		return draft && typeof draft === 'object' ? draft as Record<string, unknown> : null
	} catch {
		return null
	}
}

export function getOrCreateSubmissionId(): string {
	const draft = getCompleteAiFlowDraft()
	if (!draft) throw new Error('The completed enquiry draft is unavailable')
	if (typeof draft.submissionId === 'string' && draft.submissionId) return draft.submissionId

	const submissionId = window.crypto.randomUUID()
	window.localStorage.setItem(AI_FLOW_DRAFT.key, JSON.stringify({ ...draft, submissionId }))
	return submissionId
}

export function clearAiFlowDraft() {
	window.localStorage.removeItem(AI_FLOW_DRAFT.key)
}
