// Pathing
// _______
// src/api/crud.ts

import { client } from './client'

export function getAll<T>(pathing: string): Promise<T[]> {
	return client<T[]>(`/${pathing}/all`, { method: 'GET' })
}

export function getById<T>(pathing: string, id: string | number): Promise<T> {
	return client<T>(`/${pathing}/${id}`, { method: 'GET' })
}

export function create<T>(pathing: string, data: unknown): Promise<T> {
	return client<T>(`/${pathing}`, { method: 'POST', body: JSON.stringify(data) })
}

export function update<T>(pathing: string, id: string | number, data: unknown): Promise<T> {
	return client<T>(`/${pathing}/${id}`, { method: 'PUT', body: JSON.stringify(data) })
}

export function deleteById(pathing: string, id: string | number): Promise<unknown> {
	return client(`/${pathing}/${id}`, { method: 'DELETE' })
}

export function deleteAll(pathing: string): Promise<unknown> {
	return client(`/${pathing}/all`, { method: 'DELETE' })
}

export function deleteAllSafe(pathing: string): Promise<unknown> {
	return client(`/${pathing}/all/safe`, { method: 'DELETE' })
}
