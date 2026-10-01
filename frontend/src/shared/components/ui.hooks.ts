// Pathing
// _______
// src/shared/components/ui.hooks.ts

import { useEffect, useRef } from 'react'

export function useDialogControls(open: boolean, onClose: () => void) {
	const closeButtonRef = useRef<HTMLButtonElement>(null)

	useEffect(() => {
		if (!open) return

		const previouslyFocused = document.activeElement instanceof HTMLElement ? document.activeElement : null
		closeButtonRef.current?.focus()

		function handleKeyDown(event: KeyboardEvent) {
			if (event.key === 'Escape') {
				event.preventDefault()
				onClose()
				return
			}

			if (event.key !== 'Tab') return
			const dialog = closeButtonRef.current?.closest('[role="dialog"]')
			const focusable = dialog?.querySelectorAll<HTMLElement>('a[href], button:not([disabled]), input:not([disabled]), select:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"])')
			if (!focusable?.length) return

			const first = focusable[0]
			const last = focusable[focusable.length - 1]
			if (event.shiftKey && document.activeElement === first) {
				event.preventDefault()
				last.focus()
			} else if (!event.shiftKey && document.activeElement === last) {
				event.preventDefault()
				first.focus()
			}
		}

		document.addEventListener('keydown', handleKeyDown)
		return () => {
			document.removeEventListener('keydown', handleKeyDown)
			previouslyFocused?.focus()
		}
	}, [open, onClose])

	return closeButtonRef
}
