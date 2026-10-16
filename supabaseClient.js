import { createClient } from '@supabase/supabase-js'

// Tu Project URL sacada de la barra de direcciones de Supabase
const supabaseUrl = 'https://rilxxbocjndfaphdebln.supabase.co' 

// Tu clave publicable que ya tenías
const supabaseKey = 'sb_publisible_60tvZpD2cc_a2FrkuV7qdg_ukkZuB3s'

// Creamos y exportamos el cliente
export const supabase = createClient(supabaseUrl, supabaseKey)