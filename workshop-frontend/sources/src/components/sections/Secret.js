import React from 'react';
import classNames from 'classnames';
import { SectionProps } from '../../utils/SectionProps';

const propTypes = {
    ...SectionProps.types
}

const defaultProps = {
    ...SectionProps.defaults
}

const Secret = ({
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

    function return_secret(){
        // Get the param "secret"
        const query = new URLSearchParams(window.location.search);
        const secret = query.get('secret');

        let to_return = ""


        if (secret === "NeverGonnaGiveYouUp!:)") {
            // No one can guess the password to connect to rick-database unless I give them the secret ;)
            to_return = "<div><br>Awesome you found the secret ! The credentials password are: https://root:RickIsTheBest@rick-database.manomano.com/</div>"
        } else if ( secret !== null) {
            to_return = "<div><br>The secret is not " + secret + "</div>"
        }
        return {__html: to_return };
    }

    return (
        <section
            {...props}
            className={outerClasses}
        >
            <div className="container-xs">
                <p className="m-0 mb-32 reveal-from-bottom" data-reveal-delay="400">
                    Do you know the rest of the super secret lyrics ?
                </p>
                <form action="/" method="get">
                    <input type="text" name="secret"></input>
                </form>
                <div dangerouslySetInnerHTML={return_secret()}></div>
            </div>
        </section>
    );
}

Secret.propTypes = propTypes;
Secret.defaultProps = defaultProps;

export default Secret;