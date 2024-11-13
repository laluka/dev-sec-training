import React from 'react';
import classNames from 'classnames';
import { SectionProps } from '../../utils/SectionProps';
import DOMPurify from "dompurify";

const propTypes = {
    ...SectionProps.types
}

const defaultProps = {
    ...SectionProps.defaults
}

const Purifier = ({
                    className,
                    topOuterDivider,
                    bottomOuterDivider,
                    topDivider,
                    bottomDivider,
                    hasBgColor,
                    invertColor,
                    ...props
                }) => {


    const outerClasses = classNames(
        'textbox section center-content',
        topOuterDivider && 'has-top-divider',
        bottomOuterDivider && 'has-bottom-divider',
        hasBgColor && 'has-bg-color',
        invertColor && 'invert-color',
        className
    );

    function return_html(){
        // Get the param "secret"
        const query = new URLSearchParams(window.location.search);
        const html = query.get('html');
        return {__html: DOMPurify.sanitize(html)};
    }

    return (
        <section
            {...props}
            className={outerClasses}
        >

            <div className="container-xs">
                <p className="m-0 mb-32 reveal-from-bottom" data-reveal-delay="400">
                    It's impossible to pop an XSS here :)
                </p>
                <form action="/dompurify" method="get">
                    <input type="text" name="html"></input>
                </form>
                <div dangerouslySetInnerHTML={return_html()}></div>
            </div>
        </section>
    );
}

Purifier.propTypes = propTypes;
Purifier.defaultProps = defaultProps;

export default Purifier;