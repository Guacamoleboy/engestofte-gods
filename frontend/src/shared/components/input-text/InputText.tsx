// Pathing
// _______
// src/shared/components/input-text/InputText.tsx

import styles from './InputText.module.css'

type InputTextProps = {
	label: string
	name: string
	type?: 'text' | 'email'
	autoComplete?: string
	required?: boolean
	multiline?: boolean
}

export default function InputText({ label, name, type = 'text', autoComplete, required = false, multiline = false }: InputTextProps) {
	const inputId = name
	return (
		<div className={styles.field}>
			<label className={styles.label} htmlFor={inputId}>{label}</label>
			{multiline
				? <textarea className={`${styles.input} ${styles.textarea}`} id={inputId} name={name} required={required} />
				: <input className={styles.input} id={inputId} name={name} type={type} autoComplete={autoComplete} required={required} />}
		</div>
	)
}
