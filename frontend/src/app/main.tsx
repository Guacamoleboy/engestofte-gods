// Pathing
// _______
// src/app/main.tsx

import React from 'react'
import ReactDOM from 'react-dom/client'
import { HelmetProvider } from 'react-helmet-async'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import { TranslationProvider } from '../shared/context/TranslationContext'
import '../shared/styles/global.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
	<React.StrictMode>
		<HelmetProvider>
			<BrowserRouter>
				<TranslationProvider>
					<App />
				</TranslationProvider>
			</BrowserRouter>
		</HelmetProvider>
	</React.StrictMode>,
)
