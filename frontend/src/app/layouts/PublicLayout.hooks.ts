// Pathing
// _______
// src/app/layouts/PublicLayout.hooks.ts

import { useState } from 'react'

export function usePublicNavigation() {
	const [isMenuOpen, setIsMenuOpen] = useState(false)

	function toggleMenu() {
		setIsMenuOpen((isOpen) => !isOpen)
	}

	function closeMenu() {
		setIsMenuOpen(false)
	}

	return { isMenuOpen, toggleMenu, closeMenu }
}
