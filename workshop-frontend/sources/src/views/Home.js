import React from 'react';
import { Helmet } from 'react-helmet'

// import sections
import Hero from '../components/sections/Hero';
import Secret from '../components/sections/Secret'

const TITLE = "Rick's Website"


const Home = () => {

  return (
    <>
        <Helmet>
            <title>{ TITLE }</title>
        </Helmet>
        <Hero className="illustration-section-01" />
        <Secret></Secret>
    </>
  );
}

export default Home;