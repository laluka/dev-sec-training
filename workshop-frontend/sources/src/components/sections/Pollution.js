import React from 'react';
import classNames from 'classnames';
import { SectionProps } from '../../utils/SectionProps';

const propTypes = {
    ...SectionProps.types
}

const defaultProps = {
    ...SectionProps.defaults
}

const Pollution = ({
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

    function isObject(obj) {
        //console.log(typeof obj);
        return typeof obj === 'function' || typeof obj === 'object';
    }

    function merge(target, source) {
        for (let key in source) {
            if (isObject(target[key]) && isObject(source[key])) {
                merge(target[key], source[key]);
            } else {
                target[key] = source[key];
            }
        }
        return target;
    }

    function clone(target) {
        return merge({"favorite_singer":"Rick", "favorite_song":"RickRoll"}, target);
    }

    function is_admin(){
        const query = new URLSearchParams(window.location.search);
        let admin = false;

        let user_obj = JSON.parse(query.get('obj'));

        user_obj = clone(user_obj)

        if(window.admin === true) {
            return "Congratulations you're admin ! :D"
        }
        return "GO AWAY ! YOU'RE NOT ADMIN"
    }

    return (
        <section
            {...props}
            className={outerClasses}
        >

            <div className="container-xs">
                <p className="m-0 mb-32 reveal-from-bottom" data-reveal-delay="400">
                   Are you admin ?
                </p>
                <form action="/pollution" method="get">
                    <input type="text" name="obj"></input>
                </form>
                <div>{is_admin()}</div>
            </div>
        </section>
    );
}

Pollution.propTypes = propTypes;
Pollution.defaultProps = defaultProps;

export default Pollution;