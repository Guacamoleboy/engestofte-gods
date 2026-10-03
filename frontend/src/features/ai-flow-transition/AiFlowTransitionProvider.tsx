// Pathing
// _______
// src/features/ai-flow-transition/AiFlowTransitionProvider.tsx

import { useCallback, useEffect, useMemo, useState, type ReactNode } from 'react'
import { useNavigate } from 'react-router-dom'
import type { AiFlowDraftStatus } from '../../shared/data/aiFlowDraft'
import { AiFlowTransitionContext } from './AiFlowTransitionContext'
import PreloaderAiFlowEntry from './PreloaderAiFlowEntry'
import PreloaderAiFlowFinal from './PreloaderAiFlowFinal'

type TransitionState = { customerName?: string; destination: string; draftNotice?: Exclude<AiFlowDraftStatus, 'none'>; phase: 'filling' | 'revealing'; type: 'entry' | 'final' } | null
type AiFlowTransitionProviderProps = { children: ReactNode }

export default function AiFlowTransitionProvider({ children }: AiFlowTransitionProviderProps) {
	const navigate = useNavigate()
	const [transition, setTransition] = useState<TransitionState>(null)

	useEffect(() => {
		if (!transition || transition.phase !== 'filling') return

		const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
		const fillDuration = reducedMotion ? 180 : transition.type === 'entry' ? 1000 : 1900
		const revealTimeout = window.setTimeout(() => {
			navigate(transition.destination, {
				state: transition.customerName || transition.draftNotice
					? { customerName: transition.customerName, draftNotice: transition.draftNotice }
					: null,
			})
			setTransition((current) => current ? { ...current, phase: 'revealing' } : null)
		}, fillDuration)
		return () => window.clearTimeout(revealTimeout)
	}, [navigate, transition])

	useEffect(() => {
		if (!transition || transition.phase !== 'revealing') return
		const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
		const revealDuration = reducedMotion ? 180 : transition.type === 'entry' ? 450 : 312
		const clearTimeoutId = window.setTimeout(() => setTransition(null), revealDuration)
		return () => window.clearTimeout(clearTimeoutId)
	}, [transition])

	const startEntryTransition = useCallback(() => {
		setTransition({ destination: '/ai-flow', phase: 'filling', type: 'entry' })
	}, [])

	const startFinalTransition = useCallback((customerName: string, draftNotice?: Exclude<AiFlowDraftStatus, 'none'>) => {
		setTransition({ customerName, destination: '/ai-flow/redirect', draftNotice, phase: 'filling', type: 'final' })
	}, [])
	const contextValue = useMemo(() => ({ startEntryTransition, startFinalTransition }), [startEntryTransition, startFinalTransition])

	return <AiFlowTransitionContext.Provider value={contextValue}>
		{children}
		{transition?.type === 'entry' && <PreloaderAiFlowEntry phase={transition.phase} />}
		{transition?.type === 'final' && <PreloaderAiFlowFinal phase={transition.phase} />}
	</AiFlowTransitionContext.Provider>
}
