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
	options?: { label: string; value: string }[]
	onChange?: (value: string) => void
}

export default function InputText({ label, name, type = 'text', autoComplete, required = false, multiline = false, value, placeholder, disabled = false, min, max, step, options, onChange }: InputTextProps) {
	const inputId = name
	const handleChange = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => onChange?.(event.currentTarget.value)
	return (
		<div className={styles.field}>
			<label className={styles.label} htmlFor={inputId}>{label}</label>
			{options
				? <select className={styles.input} id={inputId} name={name} required={required} value={value} disabled={disabled} onChange={handleChange}>
					<option value="">{placeholder}</option>
					{options.map((option) => <option key={option.value} value={option.value}>{option.label}</option>)}
				</select>
				: multiline
				? <textarea className={`${styles.input} ${styles.textarea}`} id={inputId} name={name} required={required} value={value} placeholder={placeholder} disabled={disabled} onChange={handleChange} />
				: <input className={styles.input} id={inputId} name={name} type={type} autoComplete={autoComplete} required={required} value={value} placeholder={placeholder} disabled={disabled} min={min} max={max} step={step} onChange={handleChange} />}
		</div>
	)
}
