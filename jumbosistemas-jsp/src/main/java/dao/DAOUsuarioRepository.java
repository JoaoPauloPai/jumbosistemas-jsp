package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import bean.BeanGraficoSalarioUser;
import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOUsuarioRepository {

	private Connection connection;
	


		public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
		
		public BeanGraficoSalarioUser montarGraficoMediaSalario(Long userLogado) throws Exception {
			
			String sql = "select avg(rendamansal) as media_salarial, perfil from model_login were usuario_id = ? group by perfil ";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setLong(1, userLogado);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			List<String> perfils = new ArrayList<String>();
			List<Double> salarios = new ArrayList<Double>();
			
			BeanGraficoSalarioUser beanGraficoSalarioUser = new BeanGraficoSalarioUser();
			
			while(resultSet.next()) {
				Double media_salarial = resultSet.getDouble("media_salarial");
				String perfil = resultSet.getString("perfil");
				perfils.add(perfil);
				salarios.add(media_salarial);
			}
			
			beanGraficoSalarioUser.setPerfils(perfils);
			beanGraficoSalarioUser.setSalarios(salarios);
			
			return beanGraficoSalarioUser;
		}

	public ModelLogin gravarUsuario(ModelLogin objeto, Long userLogado) throws Exception {

		if (objeto.isNovo()) {/* Grava um novo */

			String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal)  VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement preparedSql = connection.prepareStatement(sql);

			preparedSql.setString(1, objeto.getLogin());
			preparedSql.setString(2, objeto.getSenha());
			preparedSql.setString(3, objeto.getNome());
			preparedSql.setString(4, objeto.getEmail());
			preparedSql.setLong(5, userLogado);
			preparedSql.setString(6, objeto.getPerfil());
			preparedSql.setString(7, objeto.getSexo());
			preparedSql.setString(8, objeto.getCep());
			preparedSql.setString(9, objeto.getLogradouro());
			preparedSql.setString(10, objeto.getBairro());
			preparedSql.setString(11, objeto.getLocalidade());
			preparedSql.setString(12, objeto.getUf());
			preparedSql.setString(13, objeto.getNumero());
			preparedSql.setDate(14, objeto.getDataNascimento());
			preparedSql.setDouble(15, objeto.getRendaMensal());

			preparedSql.execute();

			connection.commit();

			if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {
				sql = "update model_login set fotouser =?, extensaofotouser=? where login =?";

				preparedSql = connection.prepareStatement(sql);

				preparedSql.setString(1, objeto.getFotouser());
				preparedSql.setString(2, objeto.getExtensaofotouser());
				preparedSql.setString(3, objeto.getLogin());

				preparedSql.execute();

				connection.commit();
			}

		} else {
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro =?, localidade=?, uf=?, numero =?, datanascimento =?, rendamensal =? WHERE id =  "
					+ objeto.getId() + ";";

			PreparedStatement prepareSql = connection.prepareStatement(sql);

			prepareSql.setString(1, objeto.getLogin());
			prepareSql.setString(2, objeto.getSenha());
			prepareSql.setString(3, objeto.getNome());
			prepareSql.setString(4, objeto.getEmail());
			prepareSql.setString(5, objeto.getPerfil());
			prepareSql.setString(6, objeto.getSexo());
			prepareSql.setString(7, objeto.getCep());
			prepareSql.setString(8, objeto.getLogradouro());
			prepareSql.setString(9, objeto.getBairro());
			prepareSql.setString(10, objeto.getLocalidade());
			prepareSql.setString(11, objeto.getUf());
			prepareSql.setString(12, objeto.getNumero());
			prepareSql.setDate(13, objeto.getDataNascimento());
			prepareSql.setDouble(14, objeto.getRendaMensal());

			prepareSql.executeUpdate();

			connection.commit();

			if (objeto.getFotouser() != null && !objeto.getFotouser().isEmpty()) {
				sql = "update model_login set fotouser =?, extensaofotouser=? where id =?";

				prepareSql = connection.prepareStatement(sql);

				prepareSql.setString(1, objeto.getFotouser());
				prepareSql.setString(2, objeto.getExtensaofotouser());
				prepareSql.setLong(3, objeto.getId());

				prepareSql.execute();

				connection.commit();
			}

		}

		return this.consultaUsuario(objeto.getLogin(), userLogado);
	}

	public List<ModelLogin> consultaUsuarioListPaginada(Long userLogado, Integer offset) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado
				+ " order by nome offset " + offset + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* percorrer as linhas de resultado do SQL */

			ModelLogin modelLogin = new ModelLogin();

			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			// modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public int totalPagina(Long userLogado) throws Exception {

		String sql = "select count(1) as total from model_login  where usuario_id = " + userLogado;

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		resultado.next();

		Double cadastros = resultado.getDouble("total");

		Double porpagina = 5.0;

		Double pagina = cadastros / porpagina;

		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();

	}
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}
	
	
	public List<ModelLogin> consultaUsuarioListRel(Long userLogado, String dataInicial, String dataFinal) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<ModelLogin>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " and datanascimento >= ? and datanascimento <= ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setDate(1, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial))));
		statement.setDate(2, Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal))));
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) { /*percorrer as linhas de resultado do SQL*/
			
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			//modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			modelLogin.setTelefones(this.listFone(modelLogin.getId()));
			
			retorno.add(modelLogin);
		}
		
		
		return retorno;
	}

	public List<ModelLogin> consultaUsuarioList(Long userLogado) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login where useradmin is false and usuario_id = " + userLogado + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* percorrer as linhas de resultado do SQL */

			ModelLogin modelLogin = new ModelLogin();

			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			// modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public int consultaUsuarioListTotalPaginaPaginacao(String nome, Long userLogado) throws Exception {

		String sql = "select count(1) as total from model_login  where upper(nome) like upper(?) and useradmin is false and usuario_id = ? ";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		resultado.next();

		Double cadastros = resultado.getDouble("total");

		Double porpagina = 5.0;

		Double pagina = cadastros / porpagina;

		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();

	}

	public List<ModelLogin> consultaUsuarioListOffSet(String nome, Long userLogado, int offset) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login  where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "
				+ offset + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* percorrer as linhas de resultado do SQL */

			ModelLogin modelLogin = new ModelLogin();

			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			// modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public List<ModelLogin> consultaUsuarioList(String nome, Long userLogado) throws Exception {

		List<ModelLogin> retorno = new ArrayList<ModelLogin>();

		String sql = "select * from model_login  where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, userLogado);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) { /* percorrer as linhas de resultado do SQL */

			ModelLogin modelLogin = new ModelLogin();

			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			// modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}

		return retorno;
	}

	public ModelLogin consultaUsuarioLogado(String login) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "')";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resutlado = statement.executeQuery();

		while (resutlado.next()) /* Se tem resultado */ {

			modelLogin.setId(resutlado.getLong("id"));
			modelLogin.setEmail(resutlado.getString("email"));
			modelLogin.setLogin(resutlado.getString("login"));
			modelLogin.setSenha(resutlado.getString("senha"));
			modelLogin.setNome(resutlado.getString("nome"));
			modelLogin.setUseradmin(resutlado.getBoolean("useradmin"));
			modelLogin.setPerfil(resutlado.getString("perfil"));
			modelLogin.setSexo(resutlado.getString("sexo"));
			modelLogin.setFotouser(resutlado.getString("fotouser"));
			modelLogin.setCep(resutlado.getString("cep"));
			modelLogin.setLogradouro(resutlado.getString("logradouro"));
			modelLogin.setBairro(resutlado.getString("bairro"));
			modelLogin.setLocalidade(resutlado.getString("localidade"));
			modelLogin.setUf(resutlado.getString("uf"));
			modelLogin.setNumero(resutlado.getString("numero"));
			modelLogin.setDataNascimento(resutlado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resutlado.getDouble("rendamensal"));
		}

		return modelLogin;

	}

	public ModelLogin consultaUsuario(String login) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false ";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resutlado = statement.executeQuery();

		while (resutlado.next()) /* Se tem resultado */ {

			modelLogin.setId(resutlado.getLong("id"));
			modelLogin.setEmail(resutlado.getString("email"));
			modelLogin.setLogin(resutlado.getString("login"));
			modelLogin.setSenha(resutlado.getString("senha"));
			modelLogin.setNome(resutlado.getString("nome"));
			modelLogin.setUseradmin(resutlado.getBoolean("useradmin"));
			modelLogin.setPerfil(resutlado.getString("perfil"));
			modelLogin.setSexo(resutlado.getString("sexo"));
			modelLogin.setFotouser(resutlado.getString("fotouser"));
			modelLogin.setCep(resutlado.getString("cep"));
			modelLogin.setLogradouro(resutlado.getString("logradouro"));
			modelLogin.setBairro(resutlado.getString("bairro"));
			modelLogin.setLocalidade(resutlado.getString("localidade"));
			modelLogin.setUf(resutlado.getString("uf"));
			modelLogin.setNumero(resutlado.getString("numero"));
			modelLogin.setDataNascimento(resutlado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resutlado.getDouble("rendamensal"));
		}

		return modelLogin;

	}

	public ModelLogin consultaUsuario(String login, Long userLogado) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where upper(login) = upper('" + login
				+ "') and useradmin is false and usuario_id = " + userLogado;

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resutlado = statement.executeQuery();

		while (resutlado.next()) /* Se tem resultado */ {

			modelLogin.setId(resutlado.getLong("id"));
			modelLogin.setEmail(resutlado.getString("email"));
			modelLogin.setLogin(resutlado.getString("login"));
			modelLogin.setSenha(resutlado.getString("senha"));
			modelLogin.setNome(resutlado.getString("nome"));
			modelLogin.setPerfil(resutlado.getString("perfil"));
			modelLogin.setSexo(resutlado.getString("sexo"));
			modelLogin.setFotouser(resutlado.getString("fotouser"));
			modelLogin.setCep(resutlado.getString("cep"));
			modelLogin.setLogradouro(resutlado.getString("logradouro"));
			modelLogin.setBairro(resutlado.getString("bairro"));
			modelLogin.setLocalidade(resutlado.getString("localidade"));
			modelLogin.setUf(resutlado.getString("uf"));
			modelLogin.setNumero(resutlado.getString("numero"));
			modelLogin.setDataNascimento(resutlado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resutlado.getDouble("rendamensal"));
		}

		return modelLogin;

	}

	public ModelLogin consultaUsuarioID(Long id) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);

		ResultSet resutlado = statement.executeQuery();

		while (resutlado.next()) /* Se tem resultado */ {

			modelLogin.setId(resutlado.getLong("id"));
			modelLogin.setEmail(resutlado.getString("email"));
			modelLogin.setLogin(resutlado.getString("login"));
			modelLogin.setSenha(resutlado.getString("senha"));
			modelLogin.setNome(resutlado.getString("nome"));
			modelLogin.setPerfil(resutlado.getString("perfil"));
			modelLogin.setSexo(resutlado.getString("sexo"));
			modelLogin.setFotouser(resutlado.getString("fotouser"));
			modelLogin.setExtensaofotouser(resutlado.getString("extensaofotouser"));
			modelLogin.setCep(resutlado.getString("cep"));
			modelLogin.setLogradouro(resutlado.getString("logradouro"));
			modelLogin.setBairro(resutlado.getString("bairro"));
			modelLogin.setLocalidade(resutlado.getString("localidade"));
			modelLogin.setUf(resutlado.getString("uf"));
			modelLogin.setNumero(resutlado.getString("numero"));
			modelLogin.setDataNascimento(resutlado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resutlado.getDouble("rendamensal"));
		}

		return modelLogin;

	}

	public ModelLogin consultaUsuarioID(String id, Long userLogado) throws Exception {

		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, userLogado);

		ResultSet resutlado = statement.executeQuery();

		while (resutlado.next()) /* Se tem resultado */ {

			modelLogin.setId(resutlado.getLong("id"));
			modelLogin.setEmail(resutlado.getString("email"));
			modelLogin.setLogin(resutlado.getString("login"));
			modelLogin.setSenha(resutlado.getString("senha"));
			modelLogin.setNome(resutlado.getString("nome"));
			modelLogin.setPerfil(resutlado.getString("perfil"));
			modelLogin.setSexo(resutlado.getString("sexo"));
			modelLogin.setFotouser(resutlado.getString("fotouser"));
			modelLogin.setExtensaofotouser(resutlado.getString("extensaofotouser"));
			modelLogin.setCep(resutlado.getString("cep"));
			modelLogin.setLogradouro(resutlado.getString("logradouro"));
			modelLogin.setBairro(resutlado.getString("bairro"));
			modelLogin.setLocalidade(resutlado.getString("localidade"));
			modelLogin.setUf(resutlado.getString("uf"));
			modelLogin.setNumero(resutlado.getString("numero"));
			modelLogin.setDataNascimento(resutlado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resutlado.getDouble("rendamensal"));
		}

		return modelLogin;

	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" + login + "');";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resutlado = statement.executeQuery();

		resutlado.next();/* Pra ele entrar nos resultados do sql */
		return resutlado.getBoolean("existe");

	}

	public void deletarUser(String idUser) throws Exception {
		String sql = "DELETE FROM model_login WHERE id = ? and useradmin is false;";

		PreparedStatement prepareSql = connection.prepareStatement(sql);

		prepareSql.setLong(1, Long.parseLong(idUser));

		prepareSql.executeUpdate();

		connection.commit();

	}
	
	public List<ModelTelefone> listFone(Long idUserPai) throws Exception {
		
		List<ModelTelefone> retorno = new ArrayList<ModelTelefone>();
		
		String sql = "select * from telefone where usuario_pai_id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		
		preparedStatement.setLong(1, idUserPai);
		
		ResultSet rs = preparedStatement.executeQuery();
		
		while (rs.next()) {
			
			ModelTelefone modelTelefone = new ModelTelefone();
			
			modelTelefone.setId(rs.getLong("id"));
			modelTelefone.setNumero(rs.getString("numero"));
			modelTelefone.setUsuario_cad_id(this.consultaUsuarioID(rs.getLong("usuario_cad_id")));
			modelTelefone.setUsuario_pai_id(this.consultaUsuarioID(rs.getLong("usuario_pai_id")));
			
			retorno.add(modelTelefone);
			
		}
		
		return retorno;
	}

}
