// Pathing
// _______
// src/shared/components/ui.tsx

import type { ButtonHTMLAttributes, InputHTMLAttributes, ReactNode } from 'react'
import { Link } from 'react-router-dom'
import { useDialogControls } from './ui.hooks'

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
	variant?: 'primary' | 'secondary' | 'quiet'
	as?: 'button' | 'link'
	to?: string
}

export function Button({ children, className = '', variant = 'primary', as = 'button', to, ...props }: ButtonProps) {
	const classes = `ui-button ui-button--${variant}${className ? ` ${className}` : ''}`
	if (as === 'link' && to) return <Link className={classes} to={to}>{children}</Link>
	return <button className={classes} {...props}>{children}</button>
}

type TextInputProps = InputHTMLAttributes<HTMLInputElement> & {
	label: string
	error?: string
}

export function TextInput({ id, label, error, className = '', ...props }: TextInputProps) {
	const inputId = id ?? props.name ?? label.toLocaleLowerCase('da').replaceAll(' ', '-')
	return (
		<div className="ui-field">
			<label className="ui-field__label" htmlFor={inputId}>{label}</label>
			<input className={`ui-input${error ? ' has-error' : ''}${className ? ` ${className}` : ''}`} id={inputId} aria-invalid={Boolean(error)} aria-describedby={error ? `${inputId}-error` : undefined} {...props} />
			{error && <span className="ui-field__error" id={`${inputId}-error`}>{error}</span>}
		</div>
	)
}

type CardProps = {
	children: ReactNode
	className?: string
}

export function Card({ children, className = '' }: CardProps) {
	return <section className={`ui-card${className ? ` ${className}` : ''}`}>{children}</section>
}

type StatusBadgeProps = {
	children: ReactNode
	tone?: 'neutral' | 'success' | 'warning' | 'error'
}

export function StatusBadge({ children, tone = 'neutral' }: StatusBadgeProps) {
	return <span className={`ui-status ui-status--${tone}`}>{children}</span>
}

type DialogProps = {
	open: boolean
	labelledBy: string
	children: ReactNode
	onClose: () => void
}

export function Dialog({ open, labelledBy, children, onClose }: DialogProps) {
	const dialogRef = useDialogControls(open, onClose)
	if (!open) return null
	return (
		<div className="ui-dialog-backdrop" onMouseDown={(event) => { if (event.target === event.currentTarget) onClose() }}>
			<section className="ui-dialog" role="dialog" aria-modal="true" aria-labelledby={labelledBy}>
				<button ref={dialogRef} className="ui-dialog__close" type="button" aria-label="Luk dialog" onClick={onClose}>×</button>
				{children}
			</section>
		</div>
	)
}

type StateProps = {
	title: string
	description?: string
	action?: ReactNode
}

export function LoadingState({ title = 'Indlæser', description = 'Vent et øjeblik.' }: Partial<StateProps>) {
	return <div className="ui-state" role="status"><span className="ui-spinner" aria-hidden="true" /><h2>{title}</h2><p>{description}</p></div>
}

export function EmptyState({ title, description, action }: StateProps) {
	return <div className="ui-state"><span className="ui-state__mark" aria-hidden="true">✳</span><h2>{title}</h2>{description && <p>{description}</p>}{action}</div>
}

export function ErrorState({ title = 'Noget gik galt', description = 'Prøv igen om lidt.' }: Partial<StateProps>) {
	return <div className="ui-state ui-state--error" role="alert"><span className="ui-state__mark" aria-hidden="true">!</span><h2>{title}</h2><p>{description}</p></div>
}
