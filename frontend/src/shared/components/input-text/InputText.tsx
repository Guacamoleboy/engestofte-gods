// Pathing
// _______
// src/shared/components/input-text/InputText.tsx

import styles from './InputText.module.css'
import type { ChangeEvent } from 'react'

type InputTextProps = {
	label: string
	name: string
	type?: 'text' | 'email' | 'password' | 'number'
	autoComplete?: string
	required?: boolean
	multiline?: boolean
	value?: string
	placeholder?: string
	disabled?: boolean
	min?: number
	max?: number
	step?: number
	onChange?: (value: string) => void
}

export default function InputText({ label, name, type = 'text', autoComplete, required = false, multiline = false, value, placeholder, disabled = false, min, max, step, onChange }: InputTextProps) {
	const inputId = name
	const handleChange = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => onChange?.(event.currentTarget.value)
	return (
		<div className={styles.field}>
			<label className={styles.label} htmlFor={inputId}>{label}</label>
			{multiline
				? <textarea className={`${styles.input} ${styles.textarea}`} id={inputId} name={name} required={required} value={value} placeholder={placeholder} disabled={disabled} onChange={handleChange} />
				: <input className={styles.input} id={inputId} name={name} type={type} autoComplete={autoComplete} required={required} value={value} placeholder={placeholder} disabled={disabled} min={min} max={max} step={step} onChange={handleChange} />}
		</div>
	)
}
