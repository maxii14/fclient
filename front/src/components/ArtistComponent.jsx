/*import React, { useState } from "react";
import BackendService from "../services/BackendService";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft, faSave } from "@fortawesome/free-solid-svg-icons";
import { alertActions } from "../utils/Rdx";
import { connect } from "react-redux";
import { Form } from "react-bootstrap";
import { useParams, useNavigate } from 'react-router-dom';

const ArtistComponent = props => {

    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const [name, setName] = useState("")
    const [century, setCentury] = useState()
    const [country, setCountry] = useState()
    const [id, setId] = useState(useParams().id)

    const updateName = (event) => {
        setName(event.target.value)
    }

    const updateCountry = (event) => {
        setCountry(event.target.value)
    }

    const updateCentury = (event) => {
        setCentury(event.target.value)
    }

    const onSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        let err = null;
        if (name === "") {
            err = "Укажите имя"
        }
        let artist = { name: name, century: century, countryid: country}
        if (parseInt(id) == -1) {
            BackendService.createArtist(artist)
                .catch(() => { })
        }
        else {
            let artist = { name: name, id: id, century: century, countryid: country}
            BackendService.updateArtist(artist)
                .catch(() => { })
        }
        navigateArtists()
    }

    const navigateArtists = () => {
        navigate('/artists')
    }

    if (hidden)
        return null;
    return (
        <div className="m-4">
            <div className="row my-2 mr-0">
                <h3>Добавить художника</h3>
                <button
                    className="btn btn-outline-secondary ml-auto"
                    onClick={() => navigateArtists()}><FontAwesomeIcon
                        icon={faChevronLeft} />{' '}Назад</button>
            </div>
            <Form onSubmit={onSubmit}>
                <Form.Group>
                    <Form.Label>Имя, страна и век художника</Form.Label>

                    <Form.Control
                        type="text"
                        placeholder="Имя художника"
                        onChange={updateName}
                        value={name}
                        name="name"
                        autoComplete="off" />
                    <Form.Control
                        type="text"
                        placeholder="Страна художника"
                        onChange={updateCountry}
                        value={country}
                        name="name"
                        autoComplete="off" />
                    <Form.Control
                        type="int"
                        placeholder="Век художника"
                        onChange={updateCentury}
                        value={century}
                        name="name"
                        autoComplete="off" />
                </Form.Group>
                <button
                    className="btn btn-outline-secondary"
                    type="submit"><FontAwesomeIcon
                        icon={faSave} />{' '}Сохранить</button>
            </Form>
        </div>
    )

}

export default ArtistComponent;*/
import React, {useId, useState, useEffect} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronLeft, faSave} from "@fortawesome/free-solid-svg-icons";
import {alertActions} from "../utils/Rdx";
import {connect, useDispatch} from "react-redux";
import BackendService from "../services/BackendService";

const ArtistComponent = props => {

    const [Name, setName] = useState('');
    const [Century, setCentury] = useState('');
    const [Country, setCountry] = useState(0);
    const [Countries, setCountries] = useState([]);
    const [hidden, setHidden] = useState(false);
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const nameId = useId();
    const centId = useId();
    const countryId = useId();
    const params = useParams();

    const countryList = () => {
        
        console.log(Countries); 

        return Countries.map((c) => { 
            return <option key={c.id} value={c.id}> {c.name} </option>
        });
    }

    const loadArtist = () => {
        BackendService.retrieveArtist(params.id)
            .then(
                resp => {
                    setName(resp.data.name);
                    setCentury(resp.data.century);
                    setCountry(resp.data.country.id);
                    setHidden(false);
                })
            .catch(() => {
                setHidden(true)
            })
    }
    const loadCountries = () => {
        console.log("1234");
        BackendService.retrieveAllCountries(0, 8)
            .then(
                resp => {
                    setCountries(resp.data.content);
                }
            )
    }

    useEffect(() => {
        console.log("fdsfsdfds");
        loadCountries();
        if (parseInt(params.id) !== -1) {
            loadArtist();
        }
    }, [])

    const onSubmit = e => {
        e.preventDefault();
        e.stopPropagation();
  
        /*const form = e.target;
        const formData = new FormData(form);
        const formJson = Object.fromEntries(formData.entries());
        console.log("formJson = " + JSON.stringify(formJson))
        let err = null;
        if (!formJson.name) {
            err = "Имя художника должно быть указано";
        }
        if (err) {
            dispatch(alertActions.error(err))
        }
        let artist = { id: params.id,
            name: formJson.name,
            century: formJson.century,
            country: { id: formJson.country }
        };*/
        let artist = {id: params.id, name: Name, century: Century, country: {id: countryId}};
        if (parseInt(artist.id) === -1) {
            BackendService.createArtist(artist)
                .then(()=> navigate("/artists"))
                .catch(()=> {})
        }
        else {
            BackendService.updateArtist(artist)
                .then(()=> navigate("/artists"))
                .catch(()=>{})
        }
    }

   
    if (hidden)
        return null;
    return (
        <div className="container">
            <div className="row">
                <div className="col-md-6">
                    <h3>Художник</h3>
                </div>
                <div className="col-md-6 clearfix">
                <button className="btn btn-outline-secondary float-end"
                        onClick={ () => { navigate(-1) } }>
                    <FontAwesomeIcon icon={faChevronLeft}/>{' '}Назад</button>
                </div>
            </div>
            <div className="row">
                <div className="col-md-12">
                    <form method="post" onSubmit={onSubmit}>
                        <label className="form-label" htmlFor={nameId}>Имя:</label>
                        <input id={nameId} name="name"
                               className="form-control"
                               defaultValue={Name}
                               autoComplete="off"/>
                        <label className="form-label" htmlFor={centId}>Век:</label>
                        <input id={centId} name="century"
                               className="form-control"
                               defaultValue={Century}
                               autoComplete="off"/>
                        <label className="form-label" htmlFor={countryId}>Страна:</label>
                        <select id={countryId}
                                name="country"
                                className="form-control"
                                onChange={(e) => {
                                    console.log(e.target.value);
                                    setCountry(e.target.value);
                                }}
                                value={Country}>
                            {countryList()}
                        </select>
                        <button
                            className="btn btn-outline-secondary mt-4"
                            type="submit">
                            <FontAwesomeIcon icon={faSave}/>{' '}Сохранить</button>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default connect()(ArtistComponent);