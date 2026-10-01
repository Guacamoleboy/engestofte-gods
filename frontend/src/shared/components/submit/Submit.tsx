// Pathing
// _______
// src/shared/components/submit/Submit.tsx

import type { ButtonHTMLAttributes, ReactNode } from 'react'
import { Button } from '../ui'

type SubmitProps = Omit<ButtonHTMLAttributes<HTMLButtonElement>, 'type' | 'children'> & {
	children: ReactNode
}

export default function Submit({ children, className = '', ...props }: SubmitProps) {
	return <Button className={className} type="submit" {...props}>{children}</Button>
}
