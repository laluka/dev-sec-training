import React from 'react';
import classNames from 'classnames';
import { SectionProps } from '../../utils/SectionProps';
import Button from "../elements/Button";
import ButtonGroup from "../elements/ButtonGroup";

const propTypes = {
    ...SectionProps.types
}

const defaultProps = {
    ...SectionProps.defaults
}

const Hijack = ({
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

    function delete_db(){
       alert("Congratulations ! You just delete the entire database :)")
    }

    return (
        <section
            {...props}
            className={outerClasses}
        >
            <div className="container-xs">
                <p className="m-0 mb-32 reveal-from-bottom" data-reveal-delay="400">
                    Are you really really really sure you want to delete the database ?
                </p>
                <ButtonGroup>
                    <Button tag="a" color="primary" wideMobile onClick={delete_db}>
                        Delete Database
                    </Button>
                </ButtonGroup>
            </div>
        </section>
    );
}

Hijack.propTypes = propTypes;
Hijack.defaultProps = defaultProps;

export default Hijack;