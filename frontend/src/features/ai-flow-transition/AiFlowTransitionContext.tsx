// Pathing
// _______
// src/features/ai-flow-transition/AiFlowTransitionContext.tsx

import { createContext, useContext, type ReactNode } from 'react'
import type { AiFlowDraftStatus } from '../../shared/data/aiFlowDraft'

export type AiFlowTransitionContextValue = {
	startEntryTransition: () => void
	startFinalTransition: (customerName: string, draftNotice?: Exclude<AiFlowDraftStatus, 'none'>) => void
}

export const AiFlowTransitionContext = createContext<AiFlowTransitionContextValue | null>(null)

export function useAiFlowTransition() {
	const context = useContext(AiFlowTransitionContext)
	if (!context) throw new Error('AiFlowTransitionProvider is missing')
	return context
}

export type AiFlowTransitionProviderProps = { children: ReactNode }
