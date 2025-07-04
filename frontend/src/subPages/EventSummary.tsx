import {Event, service, Summary} from "../service";
import {useEffect, useState} from "react";

type Props = {
    event: Event;
    close: () => void;
}

export function EventSummary(props: Props) {

    const [summary, setSummary] = useState<Summary>();

    useEffect(() => {

        const loadSummary = async () => {
            const data = await service.getSummary(props.event.id);
            setSummary(data);
        };

        loadSummary();
    }, [props.event.id]);

    if(!summary) return <div className="component"></div>

    return (
        <div className="component">

            <h1>{props.event.title} summary</h1>

            <div>
                <h2>Total submissions: {summary!.total}</h2>
                <h2 className="positive"> Positive submissions: {summary.positive}</h2>
                <h2 className="negative">Negative submissions: {summary.negative}</h2>
                <h2> Neutral submissions: {summary.neutral}</h2>

            </div>

            <div className="bottom-buttons">
                <button className="button" onClick={props.close}>
                    Cancel
                </button>
            </div>
        </div>
    )
}