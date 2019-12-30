import React, { useState, useEffect } from "react";
import { ListGroup, Badge, Spinner } from "react-bootstrap";

const LightList = (props) => {
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([]);

    useEffect(() => {
        fetch('lamps')
            .then((res) => res.json())
            .then((res) => {
                setData(res);
                setLoading(false);
            })
            .catch(err => {
                setLoading(false);
            });
    }, [])

    if (loading) {
        return <Spinner animation="grow" />
    }

    return <ListGroup>
        {
            data.map(item => (
                <ListGroup.Item key={item.id} action onClick={(e) => {
                    setLoading(true);

                    fetch('lamp/' + item.id + '?state=' + !item.state.on, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        }
                    })
                        .then((res) => {
                            setData(data.map(d => d.id === item.id ? { ...d, state: { on: !item.state.on } } : d))
                            setLoading(false);
                        })
                        .catch(err => {
                            setLoading(false);
                        });
                }} >
                    <div>
                        <Badge variant={item.state.on === true ? "success" : "danger"}>{item.state.on === true ? "On" : "Off"}</Badge>
                        {" " + item.name}
                    </div>
                </ListGroup.Item>
            ))
        }
    </ListGroup >
}


export default LightList;