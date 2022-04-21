--
-- PostgreSQL database dump
--

-- Dumped from database version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.9 (Ubuntu 12.9-0ubuntu0.20.04.1)

--
-- Name: campeonato; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.campeonato (
    id bigint NOT NULL,
    liga integer,
    nivel_campeonato integer,
    nome character varying(255),
    id_temporada bigint,
    rodada_atual integer
);




--
-- Name: campeonato_eliminatorio; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.campeonato_eliminatorio (
    id bigint NOT NULL,
    liga integer,
    nivel_campeonato integer,
    nome character varying(255),
    id_temporada bigint,
    rodada_atual integer
);




--
-- Name: campeonato_misto; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.campeonato_misto (
    id bigint NOT NULL,
    nivel_campeonato integer,
    nome character varying(255),
    id_temporada bigint,
    rodada_atual integer
);




--
-- Name: classificacao; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.classificacao (
    id bigint NOT NULL,
    gols_pro integer,
    num_jogos integer,
    pontos integer,
    posicao integer,
    saldo_gols integer,
    vitorias integer,
    id_campeonato bigint,
    id_clube integer,
    id_grupo_campeonato bigint
);




--
-- Name: classificacao_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: clube; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.clube (
    id integer NOT NULL,
    liga integer,
    nome character varying(255),
    overhall integer
);




--
-- Name: clube_ranking; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.clube_ranking (
    id integer NOT NULL,
    ano integer,
    classificacao_continental integer,
    classificacao_copa_nacional integer,
    classificacao_nacional integer,
    posicao_geral integer,
    id_clube integer
);




--
-- Name: clube_ranking_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: grupo_campeonato; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.grupo_campeonato (
    id bigint NOT NULL,
    numero integer,
    id_campeonato_misto bigint
);




--
-- Name: habilidade_valor; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.habilidade_valor (
    id bigint NOT NULL,
    habilidade integer,
    potencial_desenvolvimento integer,
    valor integer,
    id_jogador bigint
);




--
-- Name: habilidade_valor_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: jogador; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.jogador (
    id bigint NOT NULL,
    nome character varying(255),
    numero integer,
    posicao integer,
    valor_armacao integer,
    valor_cabeceio integer,
    valor_cruzamento integer,
    valor_desarme integer,
    valor_dible integer,
    valor_dominio integer,
    valor_finalizacao integer,
    valor_forca integer,
    valor_interceptacao integer,
    valor_jogo_aereo integer,
    valor_marcacao integer,
    valor_passe integer,
    valor_posicionamento integer,
    valor_reflexo integer,
    valor_velocidade integer,
    id_clube integer,
    idade integer
);




--
-- Name: jogador_estatisticas; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.jogador_estatisticas (
    id bigint NOT NULL,
    id_habilidade_usada integer,
    vencedor boolean,
    id_jogador bigint,
    id_partida_estatisticas bigint,
    acao boolean,
    ordem integer,
    habilidade_usada integer
);




--
-- Name: jogador_estatisticas_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: jogador_grupo_estatisticas; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.jogador_grupo_estatisticas (
    id bigint NOT NULL,
    habilidade_usada integer,
    quantidade_uso integer,
    quantidade_uso_vencedor integer,
    id_jogador bigint,
    id_partida_estatisticas bigint
);




--
-- Name: jogador_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: partida_eliminatoria_resultado; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.partida_eliminatoria_resultado (
    id bigint NOT NULL,
    classificaamandante boolean,
    gols_mandante integer,
    gols_visitante integer,
    id_clube_mandante integer,
    id_clube_visitante integer,
    id_proxima_partida bigint,
    id_rodada_eliminatoria bigint
);




--
-- Name: partida_eliminatoria_resultado_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: partida_estatisticas; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.partida_estatisticas (
    id bigint NOT NULL,
    finalizacacoes_fora_mandante integer,
    finalizacacoes_fora_visitante integer,
    lances_mandante integer,
    lances_visitante integer,
    id_partida_eliminatoria_resultado bigint,
    id_partida_resultado bigint,
    finalizacacoes_defendidas_mandante integer,
    finalizacacoes_defendidas_visitante integer
);




--
-- Name: partida_estatisticas_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: partida_resultado; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.partida_resultado (
    id bigint NOT NULL,
    gols_mandante integer,
    gols_visitante integer,
    id_clube_mandante integer,
    id_clube_visitante integer,
    id_rodada bigint
);




--
-- Name: partida_resultado_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: rodada; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.rodada (
    id bigint NOT NULL,
    numero integer,
    id_campeonato bigint,
    id_grupo_campeonato bigint,
    id_semana bigint
);




--
-- Name: rodada_eliminatoria; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.rodada_eliminatoria (
    id bigint NOT NULL,
    numero integer,
    id_campeonato_eliminatorio bigint,
    id_campeonato_misto bigint,
    id_proxima_rodada bigint,
    id_semana bigint
);




--
-- Name: rodada_eliminatoria_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: rodada_seq; Type: SEQUENCE; Schema: public; Owner: fastfoot
--






--
-- Name: semana; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.semana (
    id bigint NOT NULL,
    numero integer,
    id_temporada bigint
);




--
-- Name: temporada; Type: TABLE; Schema: public; Owner: fastfoot
--

CREATE TABLE public.temporada (
    id bigint NOT NULL,
    ano integer,
    atual boolean,
    semana_atual integer
);




--
-- Name: campeonato_eliminatorio campeonato_eliminatorio_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato_eliminatorio
    ADD CONSTRAINT campeonato_eliminatorio_pkey PRIMARY KEY (id);


--
-- Name: campeonato_misto campeonato_misto_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato_misto
    ADD CONSTRAINT campeonato_misto_pkey PRIMARY KEY (id);


--
-- Name: campeonato campeonato_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato
    ADD CONSTRAINT campeonato_pkey PRIMARY KEY (id);


--
-- Name: classificacao classificacao_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.classificacao
    ADD CONSTRAINT classificacao_pkey PRIMARY KEY (id);


--
-- Name: clube clube_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.clube
    ADD CONSTRAINT clube_pkey PRIMARY KEY (id);


--
-- Name: clube_ranking clube_ranking_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.clube_ranking
    ADD CONSTRAINT clube_ranking_pkey PRIMARY KEY (id);


--
-- Name: grupo_campeonato grupo_campeonato_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.grupo_campeonato
    ADD CONSTRAINT grupo_campeonato_pkey PRIMARY KEY (id);


--
-- Name: habilidade_valor habilidade_valor_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.habilidade_valor
    ADD CONSTRAINT habilidade_valor_pkey PRIMARY KEY (id);


--
-- Name: jogador_estatisticas jogador_estatisticas_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_estatisticas
    ADD CONSTRAINT jogador_estatisticas_pkey PRIMARY KEY (id);


--
-- Name: jogador_grupo_estatisticas jogador_grupo_estatisticas_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_grupo_estatisticas
    ADD CONSTRAINT jogador_grupo_estatisticas_pkey PRIMARY KEY (id);


--
-- Name: jogador jogador_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador
    ADD CONSTRAINT jogador_pkey PRIMARY KEY (id);


--
-- Name: partida_eliminatoria_resultado partida_eliminatoria_resultado_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_eliminatoria_resultado
    ADD CONSTRAINT partida_eliminatoria_resultado_pkey PRIMARY KEY (id);


--
-- Name: partida_estatisticas partida_estatisticas_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_estatisticas
    ADD CONSTRAINT partida_estatisticas_pkey PRIMARY KEY (id);


--
-- Name: partida_resultado partida_resultado_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_resultado
    ADD CONSTRAINT partida_resultado_pkey PRIMARY KEY (id);


--
-- Name: rodada_eliminatoria rodada_eliminatoria_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada_eliminatoria
    ADD CONSTRAINT rodada_eliminatoria_pkey PRIMARY KEY (id);


--
-- Name: rodada rodada_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada
    ADD CONSTRAINT rodada_pkey PRIMARY KEY (id);


--
-- Name: semana semana_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.semana
    ADD CONSTRAINT semana_pkey PRIMARY KEY (id);


--
-- Name: temporada temporada_pkey; Type: CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.temporada
    ADD CONSTRAINT temporada_pkey PRIMARY KEY (id);


--
-- Name: rodada fk1abpuals8n2l9drrrqymco3k0; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada
    ADD CONSTRAINT fk1abpuals8n2l9drrrqymco3k0 FOREIGN KEY (id_campeonato) REFERENCES public.campeonato(id);


--
-- Name: habilidade_valor fk1vduvychqdc33hke3xg6m2s5w; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.habilidade_valor
    ADD CONSTRAINT fk1vduvychqdc33hke3xg6m2s5w FOREIGN KEY (id_jogador) REFERENCES public.jogador(id);


--
-- Name: jogador_estatisticas fk3b2j0adunpkbqkblplx2sdtr4; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_estatisticas
    ADD CONSTRAINT fk3b2j0adunpkbqkblplx2sdtr4 FOREIGN KEY (id_jogador) REFERENCES public.jogador(id);


--
-- Name: rodada fk3t8yb3gty4a4811gr76c7pcgu; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada
    ADD CONSTRAINT fk3t8yb3gty4a4811gr76c7pcgu FOREIGN KEY (id_semana) REFERENCES public.semana(id);


--
-- Name: rodada_eliminatoria fk42qlr7r1ax08hh4nfl7ykny7n; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada_eliminatoria
    ADD CONSTRAINT fk42qlr7r1ax08hh4nfl7ykny7n FOREIGN KEY (id_campeonato_eliminatorio) REFERENCES public.campeonato_eliminatorio(id);


--
-- Name: partida_resultado fk4k5n2qhtawks1iy7wuulobci2; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_resultado
    ADD CONSTRAINT fk4k5n2qhtawks1iy7wuulobci2 FOREIGN KEY (id_clube_mandante) REFERENCES public.clube(id);


--
-- Name: campeonato_eliminatorio fk5j6rwtypgudaleom79d6ur90w; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato_eliminatorio
    ADD CONSTRAINT fk5j6rwtypgudaleom79d6ur90w FOREIGN KEY (id_temporada) REFERENCES public.temporada(id);


--
-- Name: rodada fk5kkqjct0wvbp76oh24kjqnvl; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada
    ADD CONSTRAINT fk5kkqjct0wvbp76oh24kjqnvl FOREIGN KEY (id_grupo_campeonato) REFERENCES public.grupo_campeonato(id);


--
-- Name: partida_resultado fk5mkv78syevsy36u4gnywkqbmh; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_resultado
    ADD CONSTRAINT fk5mkv78syevsy36u4gnywkqbmh FOREIGN KEY (id_clube_visitante) REFERENCES public.clube(id);


--
-- Name: rodada_eliminatoria fkcy0tlxekxiw2o10axhn1nwp3k; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada_eliminatoria
    ADD CONSTRAINT fkcy0tlxekxiw2o10axhn1nwp3k FOREIGN KEY (id_semana) REFERENCES public.semana(id);


--
-- Name: partida_estatisticas fkdd2yjajt8x7gfuhv4nj0ign88; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_estatisticas
    ADD CONSTRAINT fkdd2yjajt8x7gfuhv4nj0ign88 FOREIGN KEY (id_partida_resultado) REFERENCES public.partida_resultado(id);


--
-- Name: partida_eliminatoria_resultado fkekigruq8n52f9v61n1ucrx4cx; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_eliminatoria_resultado
    ADD CONSTRAINT fkekigruq8n52f9v61n1ucrx4cx FOREIGN KEY (id_rodada_eliminatoria) REFERENCES public.rodada_eliminatoria(id);


--
-- Name: jogador_grupo_estatisticas fkf7myrudvgy354khkma2mdxn8v; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_grupo_estatisticas
    ADD CONSTRAINT fkf7myrudvgy354khkma2mdxn8v FOREIGN KEY (id_partida_estatisticas) REFERENCES public.partida_estatisticas(id);


--
-- Name: grupo_campeonato fkgdbbm7noosfhdin65oer6cvru; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.grupo_campeonato
    ADD CONSTRAINT fkgdbbm7noosfhdin65oer6cvru FOREIGN KEY (id_campeonato_misto) REFERENCES public.campeonato_misto(id);


--
-- Name: campeonato_misto fkhdrjnc2se6q2kxkhhju1rouva; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato_misto
    ADD CONSTRAINT fkhdrjnc2se6q2kxkhhju1rouva FOREIGN KEY (id_temporada) REFERENCES public.temporada(id);


--
-- Name: jogador_grupo_estatisticas fkhwdp1i3emqcxkk07l8mvsvjsj; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_grupo_estatisticas
    ADD CONSTRAINT fkhwdp1i3emqcxkk07l8mvsvjsj FOREIGN KEY (id_jogador) REFERENCES public.jogador(id);


--
-- Name: rodada_eliminatoria fkii9wwkgp03fdq5otx4gu5rot1; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada_eliminatoria
    ADD CONSTRAINT fkii9wwkgp03fdq5otx4gu5rot1 FOREIGN KEY (id_proxima_rodada) REFERENCES public.rodada_eliminatoria(id);


--
-- Name: jogador fkitetspsx8388j82flyldu7a1k; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador
    ADD CONSTRAINT fkitetspsx8388j82flyldu7a1k FOREIGN KEY (id_clube) REFERENCES public.clube(id);


--
-- Name: clube_ranking fkl7kpdnhntsiekr5klixch3q3w; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.clube_ranking
    ADD CONSTRAINT fkl7kpdnhntsiekr5klixch3q3w FOREIGN KEY (id_clube) REFERENCES public.clube(id);


--
-- Name: partida_eliminatoria_resultado fkmexalvv7jxajvjteptqm07an5; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_eliminatoria_resultado
    ADD CONSTRAINT fkmexalvv7jxajvjteptqm07an5 FOREIGN KEY (id_clube_mandante) REFERENCES public.clube(id);


--
-- Name: rodada_eliminatoria fkmlb0s6di43c8olvvdlv2wkh46; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.rodada_eliminatoria
    ADD CONSTRAINT fkmlb0s6di43c8olvvdlv2wkh46 FOREIGN KEY (id_campeonato_misto) REFERENCES public.campeonato_misto(id);


--
-- Name: classificacao fkmqhoqriwfitvviltkwtwkq3et; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.classificacao
    ADD CONSTRAINT fkmqhoqriwfitvviltkwtwkq3et FOREIGN KEY (id_grupo_campeonato) REFERENCES public.grupo_campeonato(id);


--
-- Name: semana fkn343j3fqvyoikvpg1gcq49y8i; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.semana
    ADD CONSTRAINT fkn343j3fqvyoikvpg1gcq49y8i FOREIGN KEY (id_temporada) REFERENCES public.temporada(id);


--
-- Name: partida_resultado fknc666qr25wvv1ia52hbfwhkxe; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_resultado
    ADD CONSTRAINT fknc666qr25wvv1ia52hbfwhkxe FOREIGN KEY (id_rodada) REFERENCES public.rodada(id);


--
-- Name: campeonato fko7kcwduibhj92v0alktt06jh3; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.campeonato
    ADD CONSTRAINT fko7kcwduibhj92v0alktt06jh3 FOREIGN KEY (id_temporada) REFERENCES public.temporada(id);


--
-- Name: classificacao fkoxwyd58s5cgoxgdcd7v1xxosa; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.classificacao
    ADD CONSTRAINT fkoxwyd58s5cgoxgdcd7v1xxosa FOREIGN KEY (id_campeonato) REFERENCES public.campeonato(id);


--
-- Name: partida_eliminatoria_resultado fkpd2qh1ax4lhobraqesodji1s1; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_eliminatoria_resultado
    ADD CONSTRAINT fkpd2qh1ax4lhobraqesodji1s1 FOREIGN KEY (id_clube_visitante) REFERENCES public.clube(id);


--
-- Name: partida_estatisticas fkpxbls92kw78vn6x5cn75tn7be; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_estatisticas
    ADD CONSTRAINT fkpxbls92kw78vn6x5cn75tn7be FOREIGN KEY (id_partida_eliminatoria_resultado) REFERENCES public.partida_eliminatoria_resultado(id);


--
-- Name: partida_eliminatoria_resultado fkqt0j9q5bxntrrsueefqilpb1o; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.partida_eliminatoria_resultado
    ADD CONSTRAINT fkqt0j9q5bxntrrsueefqilpb1o FOREIGN KEY (id_proxima_partida) REFERENCES public.partida_eliminatoria_resultado(id);


--
-- Name: classificacao fkqwl2349hwj35taxsos2ej7wo3; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.classificacao
    ADD CONSTRAINT fkqwl2349hwj35taxsos2ej7wo3 FOREIGN KEY (id_clube) REFERENCES public.clube(id);


--
-- Name: jogador_estatisticas fkv5vt1765hpa2d02yry5iqidi; Type: FK CONSTRAINT; Schema: public; Owner: fastfoot
--

ALTER TABLE ONLY public.jogador_estatisticas
    ADD CONSTRAINT fkv5vt1765hpa2d02yry5iqidi FOREIGN KEY (id_partida_estatisticas) REFERENCES public.partida_estatisticas(id);


--
-- PostgreSQL database dump complete
--


--###   SEQUENCES   ###


CREATE SEQUENCE public.classificacao_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.clube_ranking_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.habilidade_valor_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.jogador_estatisticas_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.jogador_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.partida_eliminatoria_resultado_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.partida_estatisticas_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.partida_resultado_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.rodada_eliminatoria_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
CREATE SEQUENCE public.rodada_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--###   PERMISSOES  ###
--ALTER TABLE public.campeonato OWNER TO fastfoot;
--ALTER TABLE public.campeonato_eliminatorio OWNER TO fastfoot;
--ALTER TABLE public.campeonato_misto OWNER TO fastfoot;
--ALTER TABLE public.classificacao OWNER TO fastfoot;
--ALTER TABLE public.classificacao_seq OWNER TO fastfoot;
--ALTER TABLE public.clube OWNER TO fastfoot;
--ALTER TABLE public.clube_ranking OWNER TO fastfoot;
--ALTER TABLE public.clube_ranking_seq OWNER TO fastfoot;
--ALTER TABLE public.grupo_campeonato OWNER TO fastfoot;
--ALTER TABLE public.habilidade_valor OWNER TO fastfoot;
--ALTER TABLE public.habilidade_valor_seq OWNER TO fastfoot;
--ALTER TABLE public.hibernate_sequence OWNER TO fastfoot;
--ALTER TABLE public.jogador OWNER TO fastfoot;
--ALTER TABLE public.jogador_estatisticas OWNER TO fastfoot;
--ALTER TABLE public.jogador_estatisticas_seq OWNER TO fastfoot;
--ALTER TABLE public.jogador_grupo_estatisticas OWNER TO fastfoot;
--ALTER TABLE public.jogador_seq OWNER TO fastfoot;
--ALTER TABLE public.partida_eliminatoria_resultado OWNER TO fastfoot;
--ALTER TABLE public.partida_eliminatoria_resultado_seq OWNER TO fastfoot;
--ALTER TABLE public.partida_estatisticas OWNER TO fastfoot;
--ALTER TABLE public.partida_estatisticas_seq OWNER TO fastfoot;
--ALTER TABLE public.partida_resultado OWNER TO fastfoot;
--ALTER TABLE public.partida_resultado_seq OWNER TO fastfoot;
--ALTER TABLE public.rodada OWNER TO fastfoot;
--ALTER TABLE public.rodada_eliminatoria OWNER TO fastfoot;
--ALTER TABLE public.rodada_eliminatoria_seq OWNER TO fastfoot;
--ALTER TABLE public.rodada_seq OWNER TO fastfoot;
--ALTER TABLE public.semana OWNER TO fastfoot;
--ALTER TABLE public.temporada OWNER TO fastfoot;