import 'tailwindcss/tailwind.css'
import '../style.css'
import Head from "next/head"
import { Fragment } from 'react'

function MyApp({ Component, pageProps }) {
  return (
    <Fragment>
      <Head>
        <link rel="manifest" href="/manifest.json" />
        <meta name="theme-color" content="#086a3e" />
        <link rel="shortcut icon" href="/favicon.ico" />
        <link rel="apple-touch-icon" sizes="192x192" href="/icongen/android-chrome-192x192.png"></link>
      </Head>
      <Component {...pageProps} />
    </Fragment>
  )
}

export default MyApp
