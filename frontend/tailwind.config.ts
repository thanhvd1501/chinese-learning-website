import type { Config } from 'tailwindcss'

const config: Config = {
  darkMode: ["class"],
  content: [
    "./app/**/*.{ts,tsx}",
    "./components/**/*.{ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        accent: {
          DEFAULT: '#FF8A3D'
        },
        border: '#EDEDED',
        orange: {
          600: '#EA580C'
        }
      },
      borderRadius: {
        xl: '0.75rem',
        '2xl': '1rem'
      },
      boxShadow: {
        soft: '0 6px 20px rgba(0,0,0,0.06)'
      },
      keyframes: {
        'slide-next': {
          '0%': { opacity: '0', transform: 'translateX(60px)' },
          '100%': { opacity: '1', transform: 'translateX(0)' }
        },
        'slide-prev': {
          '0%': { opacity: '0', transform: 'translateX(-60px)' },
          '100%': { opacity: '1', transform: 'translateX(0)' }
        }
      },
      animation: {
        'slide-next': 'slide-next 7.5s cubic-bezier(.22,.68,.46,1)',
        'slide-prev': 'slide-prev 7.5s cubic-bezier(.22,.68,.46,1)'
      }
    },
  },
  plugins: [require('tailwindcss-animate')],
}
export default config

