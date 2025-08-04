package swimmer

import com.zaxxer.hikari.HikariDataSource

import javax.sql.DataSource

import scalikejdbc.*

final class Store(context: Context):
  private val dataSource: DataSource =
    val ds = new HikariDataSource()
    ds.setDataSourceClassName(context.dataSourceClassName)
    ds.addDataSourceProperty("url", context.url)
    ds.addDataSourceProperty("user", context.user)
    ds.addDataSourceProperty("password", context.password)
    ds.setMaximumPoolSize(context.maximumPoolSize)
    ds

  ConnectionPool.singleton(DataSourceConnectionPool(dataSource))

  def listSwimmers(): List[Swimmer] =
    DB readOnly { implicit session =>
      sql"select * from swimmer order by name"
        .map(rs =>
          Swimmer(
            rs.long("id"),
            rs.string("name"), 
          )
        )
        .list()
    }

  def addSwimmer(swimmer: Swimmer): Long =
    DB localTx { implicit session =>
      sql"""
        insert into swimmer(name) values(${swimmer.name})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateSwimmer(swimmer: Swimmer): Long =
    DB localTx { implicit session =>
      sql"""
        update swimmer set name = ${swimmer.name}
        where id = ${swimmer.id}
        """
        .update()
      swimmer.id
    }

  def listSessions(swimmerId: Long): List[Session] =
    DB readOnly { implicit session =>
      sql"select * from session where swimmer_id = $swimmerId order by datetime desc"
        .map(rs =>
          Session(
            rs.long("id"),
            rs.long("swimmer_id"),
            rs.int("weight"),
            rs.string("weight_unit"),
            rs.int("laps"),
            rs.int("lap_distance"),
            rs.string("lap_unit"),
            rs.string("style"),
            rs.boolean("kickboard"),
            rs.boolean("fins"),
            rs.int("minutes"),
            rs.int("seconds"),
            rs.int("calories"),
            rs.long("datetime")
          )
        )
        .list()
    }

  def addSession(sess: Session): Long =
    DB localTx { implicit session =>
      sql"""
        insert into session(swimmer_id, weight, weight_unit, laps, lap_distance,
        lap_unit, style, kickboard, fins, minutes, seconds, calories, datetime)
        values(${sess.swimmerId}, ${sess.weight}, ${sess.weightUnit}, ${sess.laps},
        ${sess.lapDistance}, ${sess.lapUnit}, ${sess.style}, ${sess.kickboard},
        ${sess.fins}, ${sess.minutes}, ${sess.seconds}, ${sess.calories}, ${sess.datetime})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateSession(sess: Session): Long =
    DB localTx { implicit session =>
      sql"""
        update session set weight = ${sess.weight}, weight_unit = ${sess.weightUnit},
        laps = ${sess.laps}, lap_distance = ${sess.lapDistance}, lap_unit = ${sess.lapUnit},
        style = ${sess.style}, kickboard = ${sess.kickboard}, fins = ${sess.fins},
        minutes = ${sess.minutes}, seconds = ${sess.seconds}, calories = ${sess.calories},
        datetime = ${sess.datetime}
        where id = ${sess.id}
        """
        .update()
      sess.id
    }